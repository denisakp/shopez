package repository

import (
	"context"
	"github.com/Nerzal/gocloak/v13"
	"log"
	"shopez/darkshield/config"
	"shopez/darkshield/model"
	"shopez/darkshield/model/apperr"
	"strings"
)

type KeycloakAuthRepository struct {
	config *config.KeycloakConfig
}

func NewKeycloakAuthRepository(cfg *config.KeycloakConfig) *KeycloakAuthRepository {
	return &KeycloakAuthRepository{
		config: cfg,
	}
}

func (r *KeycloakAuthRepository) loginRestClient(ctx context.Context) (*gocloak.JWT, error) {
	client := gocloak.NewClient(r.config.Host)
	token, err := client.LoginClient(ctx, r.config.ClientId, r.config.ClientSecret, r.config.Realm)
	if err != nil {
		log.Printf("failed to login rest client: %v", err)
		return nil, apperr.NewInternal()
	}
	return token, nil
}

func (r *KeycloakAuthRepository) findByEmail(ctx context.Context, email string) (bool, error) {
	token, err := r.loginRestClient(ctx)
	if err != nil {
		return false, err
	}

	client := gocloak.NewClient(r.config.Host)

	users, err := client.GetUsers(ctx, token.AccessToken, r.config.Realm, gocloak.GetUsersParams{
		Username: &email,
	})
	if err != nil {
		log.Printf("failed to load users")
		return false, apperr.NewInternal()
	}

	exists := len(users) > 0
	return exists, nil
}

func (r *KeycloakAuthRepository) KeycloakUserRegister(ctx context.Context, user gocloak.User, password, role string) (*model.LoginRes, error) {
	token, err := r.loginRestClient(ctx)
	if err != nil {
		return nil, err
	}

	client := gocloak.NewClient(r.config.Host)

	exists, _ := r.findByEmail(ctx, *user.Email)
	if exists {
		return nil, apperr.NewConflict("email", *user.Email)
	}

	userId, err := client.CreateUser(ctx, token.AccessToken, r.config.Realm, user)
	if err != nil {
		log.Printf("Failed to create user %v", user.Username)
		return nil, apperr.NewInternal()
	}

	err = client.SetPassword(ctx, token.AccessToken, userId, r.config.Realm, password, false)
	if err != nil {
		log.Printf("Failed to set password for user %v", user.Username)
		return nil, apperr.NewInternal()
	}

	var lowerCaseRole = strings.ToLower(role)
	// Get the role attribute and make sure the role exists before adding it to the user
	roleAttribute, err := client.GetRealmRole(ctx, token.AccessToken, r.config.Realm, lowerCaseRole)
	if err != nil {
		log.Printf("Role %s does not exist", role)
		return nil, apperr.NewInternal()
	}

	err = client.AddRealmRoleToUser(ctx, token.AccessToken, r.config.Realm, userId, []gocloak.Role{*roleAttribute})
	if err != nil {
		log.Printf("Failed to add role %s to user", role)
		return nil, apperr.NewInternal()
	}

	// now login the user and return the jwt token
	jwt, err := r.KeycloakUserLogin(ctx, *user.Username, password)
	if err != nil {
		return nil, err
	}

	return jwt, nil
}

func (r *KeycloakAuthRepository) KeycloakUserLogin(ctx context.Context, username, password string) (*model.LoginRes, error) {
	client := gocloak.NewClient(r.config.Host)

	jwt, err := client.Login(ctx, r.config.ClientId, r.config.ClientSecret, r.config.Realm, username, password)
	if err != nil {
		log.Printf("error while executing KeycloakUserLogin:  %v", err.Error())
		return nil, apperr.NewInternal()
	}

	return &model.LoginRes{
		AccessToken:      jwt.AccessToken,
		RefreshToken:     jwt.RefreshToken,
		ExpiresIn:        jwt.ExpiresIn,
		RefreshExpiresIn: jwt.RefreshExpiresIn,
	}, nil
}
