package repository

import (
	"bytes"
	"context"
	"errors"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/credentials"
	"github.com/aws/aws-sdk-go-v2/feature/s3/manager"
	"github.com/aws/aws-sdk-go-v2/service/s3"
	"github.com/aws/aws-sdk-go-v2/service/s3/types"
	"github.com/aws/smithy-go"
	"io"
	"log"
	"mime"
	"mime/multipart"
	"path/filepath"
	minio "shopez/uploader/config"
	"shopez/uploader/utils"
	"sync"
)

type MyS3Client struct {
	client     *s3.Client
	bucketName string
}

type UploadRepository struct {
	cfg *minio.S3Configuration
}

func NewUploadRepository(cfg *minio.S3Configuration) *UploadRepository {
	return &UploadRepository{
		cfg: cfg,
	}
}

// getS3Client creates a new S3 client
// this function get the S3 client, by setting the custom endpoint and region because we're using minio
// and not the AWS S3 service. The custom endpoint and region are set in the env file
// If you intend to use the AWS S3 service, you can remove the custom endpoint and region and configure the s3 client
// according to the documentation provided here: https://docs.aws.amazon.com/code-library/latest/ug/go_2_s3_code_examples.html
func (u *UploadRepository) getS3Client() (*MyS3Client, error) {
	customResolver := aws.EndpointResolverWithOptionsFunc(func(service, region string, options ...interface{}) (aws.Endpoint, error) {
		return aws.Endpoint{
			PartitionID:       "aws",
			URL:               u.cfg.AwsEndpoint,
			SigningRegion:     u.cfg.AwsRegion,
			HostnameImmutable: true,
		}, nil
	})

	cfg, err := config.LoadDefaultConfig(
		context.TODO(),
		config.WithRegion(u.cfg.AwsRegion),
		config.WithCredentialsProvider(credentials.NewStaticCredentialsProvider(u.cfg.AwsAccessKeyId, u.cfg.AwsSecretAccessKey, "")),
		config.WithEndpointResolverWithOptions(customResolver),
	)

	if err != nil {
		log.Printf("ERROR: unable to load SDK env, %v", err)
		return nil, err
	}

	client := s3.NewFromConfig(cfg, func(o *s3.Options) {})
	return &MyS3Client{client: client, bucketName: u.cfg.AwsBucket}, nil
}

// bucketExist checks if the bucket exists
func (u *UploadRepository) bucketExist(ctx context.Context) (bool, error) {
	s3clt, err := u.getS3Client()
	if err != nil {
		return false, err
	}

	input := s3.HeadBucketInput{
		Bucket: aws.String(u.cfg.AwsBucket),
	}

	_, err = s3clt.client.HeadBucket(ctx, &input)
	if err != nil {
		var apiError smithy.APIError
		if errors.As(err, &apiError) {
			var notFoundError *types.NotFound
			switch {
			case errors.As(err, &notFoundError):
				log.Printf("WARN: bucket %s does not exist", u.cfg.AwsBucket)
				return false, nil
			default:
				log.Printf("ERROR: failed to check if bucket %s exists: %v", u.cfg.AwsBucket, err)
				return false, err
			}
		}
		log.Printf("ERROR: failed to check if bucket %s exists: %v", u.cfg.AwsBucket, err)
		return false, err
	}
	return true, nil
}

// putObject uploads the file to the bucket
func (u *UploadRepository) putObject(ctx context.Context, s3Clt *MyS3Client, largeObject []byte, fileName string) (string, error) {
	largeBuffer := bytes.NewReader(largeObject)
	var parMiBs int64 = 16

	uploader := manager.NewUploader(s3Clt.client, func(u *manager.Uploader) {
		u.PartSize = parMiBs * 1024 * 1024
		u.Concurrency = 10
	})

	contentType := mime.TypeByExtension(filepath.Ext(fileName))
	if contentType == "" {
		contentType = "application/octet-stream"
	}

	rst, err := uploader.Upload(ctx, &s3.PutObjectInput{
		Bucket:      aws.String(u.cfg.AwsBucket),
		Key:         aws.String(fileName),
		Body:        largeBuffer,
		ContentType: aws.String(contentType),
		ACL:         types.ObjectCannedACLPublicRead,
	})

	if err != nil {
		log.Printf("ERROR: failed to upload object: %v", err)
		return "", err
	}

	return rst.Location, nil
}

// S3FileUpload uploads the file to the S3 bucket
// Files are uploaded concurrently using goroutines and sync.WaitGroup.
// Errors are collected in a channel and checked after all uploads are complete, if any error is found, it is returned.
// Context is propagated through the upload process for better cancellation and deadline handling,
// and defer file.Close() is used to ensure files are closed properly
func (u *UploadRepository) S3FileUpload(ctx context.Context, items []*multipart.FileHeader) (interface{}, error) {
	s3clt, err := u.getS3Client()
	if err != nil {
		return nil, err
	}

	var (
		locations []string
		mu        sync.Mutex
		wg        sync.WaitGroup
		errChan   = make(chan error, len(items))
	)

	for _, item := range items {
		wg.Add(1)
		go func(item *multipart.FileHeader) {
			defer wg.Done()

			file, err := item.Open()
			if err != nil {
				log.Printf("ERROR: failed to open file: %v", err)
				errChan <- err
				return
			}
			defer func(file multipart.File) {
				err := file.Close()
				if err != nil {
					errChan <- err
				}
			}(file)

			fileInBytes, err := io.ReadAll(file)
			if err != nil {
				log.Printf("ERROR: failed to read file: %v", err)
				errChan <- err
				return
			}

			formatedFileName := utils.FormatFileName(item.Filename)
			location, err := u.putObject(ctx, s3clt, fileInBytes, formatedFileName)
			if err != nil {
				log.Printf("ERROR: failed to upload file %s to bucket: %v", item.Filename, err)
				errChan <- err
				return
			}

			mu.Lock()
			locations = append(locations, location)
			mu.Unlock()
		}(item)
	}
	wg.Wait()
	close(errChan)

	for err := range errChan {
		if err != nil {
			return "", err
		}
	}

	if len(locations) == 1 {
		return locations[0], nil
	}

	return locations, nil
}
