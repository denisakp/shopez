package config

import (
	"fmt"
	"github.com/joho/godotenv"
	"log"
	"os"
)

type DarkshieldConfig struct {
	KeycloakIamConfig *KeycloakConfig
	ApiVersion        string
}

func LoadConfig() (*DarkshieldConfig, error) {
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("failed to load env file: %v", err)
	}

	// load keycloak config
	keycloakCfg, _ := keycloakConfig()

	//load apiVersion
	apiVersion, err := getEnv("API_VERSION")
	if err != nil {
		return nil, err
	}

	return &DarkshieldConfig{
		KeycloakIamConfig: keycloakCfg,
		ApiVersion:        apiVersion,
	}, nil
}

func getEnv(key string) (string, error) {
	value := os.Getenv(key)
	if value == "" {
		return "", fmt.Errorf("env variable %s is not set", key)
	}

	return value, nil
}
