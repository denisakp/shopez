include .env

up:
	docker-compose up -d

build:
	docker-compose build

down:
	docker-compose down

build-darkshield:
	cd apps/backend/darkshield && \
	docker build \
	--build-arg API_VERSION=$(API_VERSION) \
	--build-arg AWS_ACCESS_KEY_ID=$(AWS_ACCESS_KEY_ID) \
	--build-arg AWS_SECRET_ACCESS_KEY=$(AWS_SECRET_ACCESS_KEY) \
	--build-arg AWS_REGION=$(AWS_REGION) \
	--build-arg AWS_BUCKET=$(AWS_BUCKET) \
	--build-arg AWS_ENDPOINT_URL=$(AWS_ENDPOINT_URL) \
	-t denisakp/shopez-darkshield .
	
build-skylift:
	cd apps/backend/skylift && \
	docker build \
	--build-arg API_VERSION=$(API_VERSION) \
	--build-arg AWS_ACCESS_KEY_ID=$(AWS_ACCESS_KEY_ID) \
	--build-arg AWS_SECRET_ACCESS_KEY=$(AWS_SECRET_ACCESS_KEY) \
	--build-arg AWS_REGION=$(AWS_REGION) \
	--build-arg AWS_BUCKET=$(AWS_BUCKET) \
	--build-arg AWS_ENDPOINT_URL=$(AWS_ENDPOINT_URL) \
	-t denisakp/shopez-skylift .
