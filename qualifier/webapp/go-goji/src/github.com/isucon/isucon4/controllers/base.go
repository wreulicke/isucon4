package controllers

import (
	"github.com/gorilla/sessions"
)

var sessionName string = "SESSION"
var store = sessions.NewCookieStore([]byte("something-very-secret"))

type BaseController struct {
}
