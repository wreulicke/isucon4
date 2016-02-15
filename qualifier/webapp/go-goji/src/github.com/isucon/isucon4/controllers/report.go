package controllers

import (
	"encoding/json"
	"net/http"

	"github.com/isucon/isucon4/models"
)

type ReportController struct {
	BaseController
}

func (controller *ReportController) Json(w http.ResponseWriter, r *http.Request) {
	encoder := json.NewEncoder(w)
	json := map[string][]string{
		"banned_ips":   models.BannedIPs(),
		"locked_users": models.LockedUsers()}
	encoder.Encode(json)
}
