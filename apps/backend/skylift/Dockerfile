FROM golang:1.22-alpine AS builder

ARG AwsSecretAccessKey
ARG AwsAccessKeyId
ARG AwsRegion
ARG AwsEndpoint
ARG AwsBucket
ARG API_VERSION

ENV AwsSecretAccessKey=$AwsSecretAccessKey
ENV AwsAccessKeyId=$AwsAccessKeyId
ENV AwsRegion=$AwsRegion
ENV AwsEndpoint=$AwsEndpoint
ENV AwsBucket=$AwsBucket

ENV API_VERSION=$API_VERSION

WORKDIR /app

COPY go.mod go.sum ./

RUN go mod download

COPY . .

ENV GOCACHE=/root/.cache/go-build
ENV GIN_MODE=release

RUN --mount=type=cache,target="/root/.cache/go-build" CGO_ENABLED=0 GOOS=linux go build -o skylift

FROM alpine:edge AS runner

RUN apk add --no-cache dumb-init

WORKDIR /skylift

COPY --from=builder /app/skylift .

EXPOSE 8000

ENTRYPOINT ["dumb-init", "--"]

CMD ["./skylift"]
