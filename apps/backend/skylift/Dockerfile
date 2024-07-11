FROM golang:1.22-alpine AS builder

ARG AWS_SECRET_ACCESS_KEY
ARG AWS_ACCESS_KEY_ID
ARG AWS_REGION
ARG AWS_ENDPOINT_URL
ARG AWS_BUCKET
ARG API_VERSION=v1

ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
    AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
    AWS_REGION=$AWS_REGION \
    AWS_ENDPOINT_URL=$AWS_ENDPOINT_URL \
    AWS_BUCKET=$AWS_BUCKET \
    API_VERSION=$API_VERSION


WORKDIR /app

COPY go.mod go.sum ./

RUN go mod download

COPY . .

ENV GIN_MODE=release

RUN --mount=type=cache,target="/root/.cache/go-build" CGO_ENABLED=0 GOOS=linux go build -o skylift

FROM alpine:edge AS runner

RUN apk add --no-cache dumb-init

ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
    AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
    AWS_REGION=$AWS_REGION \
    AWS_ENDPOINT_URL=$AWS_ENDPOINT_URL \
    AWS_BUCKET=$AWS_BUCKET \
    API_VERSION=$API_VERSION

RUN addgroup -S skylift && adduser -S skylift -G skylift

WORKDIR /skylift

COPY --from=builder /app/skylift .

RUN chown -R skylift:skylift /skylift

USER skylift

EXPOSE 80

ENTRYPOINT ["dumb-init", "--"]

CMD ["./skylift"]
