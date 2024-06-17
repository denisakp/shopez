package model

import (
	"context"
	"github.com/Nerzal/gocloak/v13"
)

// AuthenticationService defines methods the handler layer excepts
type AuthenticationService interface {
	Register(ctx context.Context, payload SignupReq) (*LoginRes, error)
	Login(ctx context.Context, payload LoginReq) (*LoginRes, error)
}

// AuthenticationRepository define methods the authentication service layer excepts from keycloak infra
type AuthenticationRepository interface {
	KeycloakUserRegister(ctx context.Context, user gocloak.User, password, role string) (*LoginRes, error)
	KeycloakUserLogin(ctx context.Context, username, password string) (*LoginRes, error)
}
