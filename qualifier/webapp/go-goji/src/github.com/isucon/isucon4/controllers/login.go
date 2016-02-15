package controllers

import (
	"fmt"
	"net/http"

	"github.com/flosch/pongo2"
	"github.com/zenazn/goji/web"

	"github.com/isucon/isucon4/models"
)

type LoginController struct {
	BaseController
}

func (controller *LoginController) Index(w http.ResponseWriter, r *http.Request) {

	session, _ := store.Get(r, sessionName)

	msg := ""
	if flashMsg := session.Flashes("msg"); len(flashMsg) > 0 {
		msg = fmt.Sprint(flashMsg[0])
	}

	login := ""
	if flashLogin := session.Flashes("login"); len(flashLogin) > 0 {
		login = fmt.Sprint(flashLogin[0])
	}

	session.Save(r, w)

	tpl, err := pongo2.DefaultSet.FromFile("src/github.com/isucon/isucon4/templates/login.html")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	err = tpl.ExecuteWriter(pongo2.Context{
		"msg":   msg,
		"login": login,
	}, w)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func (controller *LoginController) Login(c web.C, w http.ResponseWriter, r *http.Request) {

	session, _ := store.Get(r, sessionName)

	login := r.PostFormValue("login")
	password := r.PostFormValue("password")
	remoteAddr := r.RemoteAddr
	if xForwardedFor := r.Header.Get("X-Forwarded-For"); len(xForwardedFor) > 0 {
		remoteAddr = xForwardedFor
	}

	u := &models.User{}
	user, err := u.AttemptLogin(login, password, remoteAddr)
	if err != nil || user == nil {
		msg := ""

		switch err {
		case models.ErrBannedIP:
			msg = "You're banned."
		case models.ErrLockedUser:
			msg = "This account is locked."
		default:
			msg = "Wrong username or password"
		}

		session.AddFlash(msg, "msg")
		session.AddFlash(login, "login")
		session.Save(r, w)

		http.Redirect(w, r, "/", http.StatusFound)

		return
	}

	session.Values["loginId"] = user.ID

	session.Save(r, w)

	http.Redirect(w, r, "/mypage", http.StatusFound)
}
