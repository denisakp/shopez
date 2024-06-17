package model

type SignupReq struct {
	Email     string `json:"email" binding:"required,email"`
	Password  string `json:"password" binding:"required,gte=8"`
	FirstName string `json:"first_name" binding:"lte=100"`
	LastName  string `json:"last_name" binding:"lte=100"`
}

type LoginReq struct {
	Email    string `json:"email" binding:"required,email"`
	Password string `json:"password" binding:"required"`
}

type LoginRes struct {
	AccessToken      string `json:"access_token"`
	ExpiresIn        int    `json:"expires_in"`
	RefreshToken     string `json:"refresh_token"`
	RefreshExpiresIn int    `json:"refresh_expires_in"`
}

type RoleEnum string

const (
	Admin    RoleEnum = "admin"
	Customer RoleEnum = "customer"
)
