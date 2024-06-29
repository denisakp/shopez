# ShopEZ

A simple eCommerce platform showcasing microservices architecture with full DevSecOps integration.

## Table of Contents

- [ShopEZ](#shopez)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Architecture](#architecture)
  - [Microservices](#microservices)
  - [Technologies Used](#technologies-used)
  - [Setup and Installation](#setup-and-installation)
    - [Prerequisites](#prerequisites)
    - [Steps](#steps)
  - [Usage](#usage)
  - [CI/CD](#cicd)
  - [DevOps](#devops)
  - [Security](#security)
  - [Monitoring](#monitoring)
  - [Contributing](#contributing)
  - [License](#license)
        

## Introduction

ShopEZ is a practice project designed to provide hands-on experience with building, deploying, and securing a microservices architecture. It includes multiple microservices written in different programming languages, a frontend application, and a comprehensive DevOps setup.

## Architecture

<img src="path/to/architecture-diagram.png" alt="Architecture Diagram">

## Microservices

1. **User Service (Golang - Gin)**
    
    - Handles user registration, login, and profile management.
        
    - Database: PostgreSQL
        
2. **Product Service (Java - Spring Boot)**
    
    - Manages the product catalog.
        
    - Database: MongoDB
        
    - Other: implement products search and filtering.
        
3. **Order Service (Python - FastAPI)**
    
    - Handles order creation, update, and retrieval.
        
    - Database: PostgreSQL
        
4. **Payment Service (TypeScript - NestJS)**
    
    - Processes payments and handles payment status.
        
    - Database: PostgreSQL
        
5. **Frontend (Next.js)**
    
    - User interface for the eCommerce platform.
        

## Technologies Used

- **Programming Languages:** Golang, Java, Python, TypeScript
    
- **Frameworks:** Gin, Spring Boot, FastAPI, NestJS, Next.js
    
- **Databases:** PostgreSQL, MongoDB
    
- **Message Broker:** RabbitMQ
    
- **Caching:** Redis
    
- **API Gateway:** Kong Hq
    
- **CI/CD:** GitHub Actions, Jenkins, GitLab CI
    
- **Containerization:** Docker
    
- **Orchestration:** Kubernetes, Helm
    
- **Service Mesh:** Istio
    
- **Ingress Controller:** Nginx
    
- **SSL Management:** Cert-Manager
    
- **Authentication & Authorization:** Keycloak, OAuth2 Proxy
    
- **Monitoring:** Prometheus, Grafana, Zipkin, AlertManager
    
- **Infrastructure as Code:** Terraform
    

## Setup and Installation

### Prerequisites

- Docker
    
- Docker Compose
    
- Kubernetes (Minikube or DigitalOcean)
    
- Terraform
    
- Git
    

### Steps

1. **Clone the repository:**
    
    ``` bash
    git clone https://github.com/denisakp/shopez.git
    cd shopez
    
     ```
    
2. **Setup Infrastructure with Terraform:**
    
    ``` bash
    cd infrastructure
    terraform init
    terraform apply
    
     ```
    
3. **Build and run services with Docker Compose:**
    
    ``` bash
    cd ..
    docker-compose up --build
    
     ```
    
4. **Deploy to Kubernetes:**
    
    ``` bash
    kubectl apply -f kubernetes-manifests/
    helm install shop-ez helm-charts/
    
     ```
    

## Usage

- Access the frontend at `http://localhost:3000`
    
- Use the API endpoints to interact with the services (detailed in each service's README).
    

## CI/CD

This project includes automated CI/CD pipelines using GitHub Actions, Jenkins, and GitLab CI. Each pipeline performs the following tasks:

- Build and test the code.
    
- Build Docker images.
    
- Push Docker images to the container registry.
    
- Deploy to Kubernetes based on the git branch (dev, staging, main).
    

## DevOps

- **Containerization:** Each microservice is containerized using Docker.
    
- **Orchestration:** Kubernetes is used to manage the deployment of containers.
    
- **Service Mesh:** Istio ensures secure communication between services.
    
- **Ingress:** Nginx Ingress Controller manages external access.
    
- **SSL:** Cert-Manager automates SSL certificate management.
    
- **Infrastructure:** Terraform provisions the infrastructure on DigitalOcean.
    

## Security

- **Authentication and Authorization:** Keycloak with OAuth2 Proxy secures access to the administration dashboard.
    
- **Network Policies:** Ensure that applications in different namespaces access only the appropriate resources.
    
- **mTLS:** Istio ensures all communications within the cluster are encrypted.
    

## Monitoring

- **Metrics:** Prometheus collects metrics from the services.
    
- **Visualization:** Grafana visualizes metrics.
    
- **Tracing:** Zipkin traces requests across microservices.
    
- **Alerting:** AlertManager sends alerts based on Prometheus metrics.
    

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.

## License

This project is licensed under the MIT License. See the [LICENSE](https://LICENSE) file for details.

---