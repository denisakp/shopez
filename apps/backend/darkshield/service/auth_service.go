package service

import (
	"context"
	"github.com/Nerzal/gocloak/v13"
	"shopez/darkshield/model"
)

// authService acts as a bridge between the handler and the infra
type authService struct {
	AuthenticationRepository model.AuthenticationRepository
}

// ASConfig will hold the wrapper that will be injected
type ASConfig struct {
	AuthenticationRepository model.AuthenticationRepository
}

func NewAuthenticationService(cfg *ASConfig) model.AuthenticationService {
	return &authService{
		AuthenticationRepository: cfg.AuthenticationRepository,
	}
}

func (as *authService) Register(ctx context.Context, payload model.SignupReq) (*model.LoginRes, error) {

	user := gocloak.User{
		Username:   gocloak.StringP(payload.Email),
		Email:      gocloak.StringP(payload.Email),
		FirstName:  gocloak.StringP(payload.FirstName),
		LastName:   gocloak.StringP(payload.LastName),
		Enabled:    gocloak.BoolP(true),
		Attributes: &map[string][]string{},
	}

	(*user.Attributes)["avatar"] = []string{"https://www.jea.com/cdn/images/avatar/avatar-alt.svg"}

	jwt, err := as.AuthenticationRepository.KeycloakUserRegister(ctx, user, payload.Password, string(model.Customer))
	if err != nil {
		return nil, err
	}

	return jwt, nil
}

func (as *authService) Login(ctx context.Context, payload model.LoginReq) (*model.LoginRes, error) {
	jwt, err := as.AuthenticationRepository.KeycloakUserLogin(ctx, payload.Email, payload.Password)
	if err != nil {
		return nil, err
	}

	return jwt, nil
}
