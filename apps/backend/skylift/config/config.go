package config

import (
	"fmt"
	"github.com/joho/godotenv"
	"os"
)

type UploaderConfig struct {
	ApiVersion  string
	MinioConfig *S3Configuration
}

// LoadConfig loads the configuration from the environment variables
func LoadConfig() (*UploaderConfig, error) {
	err := godotenv.Load()
	if err != nil {
		return nil, fmt.Errorf("failed to load env file: %v", err)
	}

	// load minio config
	minioCfg, err := s3Configuration()
	if err != nil {
		return nil, fmt.Errorf("failed to load minio config: %v", err)
	}

	// load api version
	apiVersion, err := getEnv("API_VERSION")
	if err != nil {
		return nil, fmt.Errorf("failed to load api version: %v", err)
	}

	return &UploaderConfig{
		ApiVersion:  apiVersion,
		MinioConfig: minioCfg,
	}, nil
}

func getEnv(key string) (string, error) {
	value := os.Getenv(key)
	if value == "" {
		return "", fmt.Errorf("env variable %s is not set", key)
	}

	return value, nil
}

func getMultipleEnv(keys ...string) (map[string]string, error) {
	envs := make(map[string]string)
	for _, key := range keys {
		value, err := getEnv(key)
		if err != nil {
			return nil, err
		}
		envs[key] = value
	}
	return envs, nil
}
