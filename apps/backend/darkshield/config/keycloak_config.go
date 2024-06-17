package config

type KeycloakConfig struct {
	Host         string
	Realm        string
	ClientId     string
	ClientSecret string
}

func keycloakConfig() (*KeycloakConfig, error) {
	host, err := getEnv("KEYCLOAK_HOST")
	if err != nil {
		return nil, err
	}

	realm, err := getEnv("KEYCLOAK_REALM")
	if err != nil {
		return nil, err
	}
	clientId, err := getEnv("KEYCLOAK_CLIENT_ID")
	if err != nil {
		return nil, err
	}
	clientSecret, err := getEnv("KEYCLOAK_CLIENT_SECRET")
	if err != nil {
		return nil, err
	}

	return &KeycloakConfig{
		Host:         host,
		Realm:        realm,
		ClientId:     clientId,
		ClientSecret: clientSecret,
	}, nil
}
