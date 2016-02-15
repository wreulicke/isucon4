package controllers

import (
	"net/http"

	"github.com/flosch/pongo2"

	"github.com/isucon/isucon4/models"
)

type MyPageController struct {
	BaseController
}

func (controller *MyPageController) Index(w http.ResponseWriter, r *http.Request) {

	session, _ := store.Get(r, sessionName)

	loginId := session.Values["loginId"]
	if loginId == nil {
		session.AddFlash("You must be logged in", "msg")
		session.Save(r, w)

		http.Redirect(w, r, "/", http.StatusFound)

		return
	}

	u := &models.User{}
	currentUser := u.GetCurrentUser(loginId)

	tpl, err := pongo2.DefaultSet.FromFile("src/github.com/isucon/isucon4/templates/mypage.html")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	lastLogin := currentUser.GetLastLogin()
	err = tpl.ExecuteWriter(pongo2.Context{
		"createdAt": lastLogin.CreatedAt,
		"ip":        lastLogin.IP,
		"login":     lastLogin.Login,
	}, w)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}
