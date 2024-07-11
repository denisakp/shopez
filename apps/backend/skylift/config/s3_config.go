package config

type S3Configuration struct {
	AwsSecretAccessKey string
	AwsAccessKeyId     string
	AwsRegion          string
	AwsEndpoint        string
	AwsBucket          string
}

func s3Configuration() (*S3Configuration, error) {
	// get the required environment variables for S3 configuration
	keys := []string{
		"AWS_SECRET_ACCESS_KEY",
		"AWS_ACCESS_KEY_ID",
		"AWS_REGION",
		"AWS_ENDPOINT_URL",
		"AWS_BUCKET",
	}

	envs, err := getMultipleEnv(keys...)
	if err != nil {
		return nil, err
	}

	var cfg = &S3Configuration{
		AwsSecretAccessKey: envs["AWS_SECRET_ACCESS_KEY"],
		AwsAccessKeyId:     envs["AWS_ACCESS_KEY_ID"],
		AwsRegion:          envs["AWS_REGION"],
		AwsEndpoint:        envs["AWS_ENDPOINT_URL"],
		AwsBucket:          envs["AWS_BUCKET"],
	}

	return cfg, nil
}
