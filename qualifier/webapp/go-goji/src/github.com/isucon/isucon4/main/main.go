package main

import (
	"flag"
	"io/ioutil"
	"log"
	"net/http"

	_ "github.com/go-sql-driver/mysql"

	"github.com/zenazn/goji"
	"github.com/zenazn/goji/web"

	"github.com/isucon/isucon4/controllers"
)

func main() {

	// Setup static files
	stylesheets := web.New()
	stylesheetsPath := "src/github.com/isucon/isucon4/public/stylesheets"
	stylesheets.Get("/stylesheets/*", http.StripPrefix("/stylesheets/", http.FileServer(http.Dir(stylesheetsPath))))
	http.Handle("/stylesheets/", stylesheets)

	images := web.New()
	imagesPath := "src/github.com/isucon/isucon4/public/images"
	images.Get("/images/*", http.StripPrefix("/images/", http.FileServer(http.Dir(imagesPath))))
	http.Handle("/images/", images)

	// Setup controllers
	rootController := &controllers.LoginController{}
	myPageController := &controllers.MyPageController{}
	reportController := &controllers.ReportController{}
	goji.Get("/", rootController.Index)
	goji.Post("/login", rootController.Login)
	goji.Get("/mypage", myPageController.Index)
	goji.Get("/report", reportController.Json)

	// Listen port
	flag.Set("bind", ":8080")

	// Disable logger
	log.SetOutput(ioutil.Discard)

	goji.Serve()
}
