package models

import (
	"crypto/sha256"
	"database/sql"
	"errors"
	"fmt"
	"io"
	"time"
)

var (
	ErrBannedIP      = errors.New("Banned IP")
	ErrLockedUser    = errors.New("Locked user")
	ErrUserNotFound  = errors.New("Not found user")
	ErrWrongPassword = errors.New("Wrong password")
)

type User struct {
	ID           int
	Login        string
	PasswordHash string
	Salt         string

	LastLogin *LastLogin
}

type LastLogin struct {
	Login     string
	IP        string
	CreatedAt time.Time
}

func (u *User) GetLastLogin() *LastLogin {
	rows, err := db.Query(
		"SELECT login, ip, created_at FROM login_log WHERE succeeded = 1 AND user_id = ? ORDER BY id DESC LIMIT 2",
		u.ID,
	)

	if err != nil {
		return nil
	}

	defer rows.Close()
	for rows.Next() {
		u.LastLogin = &LastLogin{}
		err = rows.Scan(&u.LastLogin.Login, &u.LastLogin.IP, &u.LastLogin.CreatedAt)
		if err != nil {
			u.LastLogin = nil
			return nil
		}
	}

	return u.LastLogin
}

func (u *User) AttemptLogin(loginName string, password string, remoteAddr string) (*User, error) {
	succeeded := false
	user := &User{}

	defer func() {
		createLoginLog(succeeded, remoteAddr, loginName, user)
	}()

	row := db.QueryRow(
		"SELECT id, login, password_hash, salt FROM users WHERE login = ?",
		loginName,
	)
	err := row.Scan(&user.ID, &user.Login, &user.PasswordHash, &user.Salt)

	switch {
	case err == sql.ErrNoRows:
		user = nil
	case err != nil:
		return nil, err
	}

	if banned, _ := isBannedIP(remoteAddr); banned {
		return nil, ErrBannedIP
	}

	if locked, _ := isLockedUser(user); locked {
		return nil, ErrLockedUser
	}

	if user == nil {
		return nil, ErrUserNotFound
	}

	if user.PasswordHash != u.calcPassHash(password, user.Salt) {
		return nil, ErrWrongPassword
	}

	succeeded = true
	return user, nil
}

func (u *User) GetCurrentUser(userId interface{}) *User {
	user := &User{}
	row := db.QueryRow(
		"SELECT id, login, password_hash, salt FROM users WHERE id = ?",
		userId,
	)
	err := row.Scan(&user.ID, &user.Login, &user.PasswordHash, &user.Salt)

	if err != nil {
		return nil
	}

	return user
}

func (u *User) calcPassHash(password, hash string) string {
	h := sha256.New()
	io.WriteString(h, password)
	io.WriteString(h, ":")
	io.WriteString(h, hash)

	return fmt.Sprintf("%x", h.Sum(nil))
}
