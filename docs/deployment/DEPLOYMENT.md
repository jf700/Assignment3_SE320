# Digital Therapy Assistant — EC2 Deployment Guide

## Prerequisites
- AWS account with EC2 access
- SSH key pair created in AWS
- GitHub repository with the application code

## 1. Launch EC2 Instance

1. Open AWS EC2 Console
2. Launch instance:
   - **AMI**: Ubuntu 22.04 LTS or Amazon Linux 2023
   - **Instance type**: t2.medium (or t3.medium)
   - **Key pair**: Select or create an SSH key pair
3. Configure **Security Group** with inbound rules:

| Port | Protocol | Source | Purpose |
|------|----------|--------|---------|
| 22 | TCP | Your IP | SSH access |
| 3000 | TCP | 0.0.0.0/0 | Frontend (Nginx) |
| 8080 | TCP | 0.0.0.0/0 | Backend (Swagger, H2, API) |

4. **Allocate an Elastic IP** and associate it with the instance

## 2. Connect and Install Dependencies

```bash
ssh -i your-key.pem ubuntu@<elastic-ip>

# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
sudo apt install -y docker.io docker-compose-plugin
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER

# Log out and back in for group changes
exit
ssh -i your-key.pem ubuntu@<elastic-ip>

# Install Git
sudo apt install -y git
```

## 3. Deploy the Application

```bash
# Clone repository
git clone <your-github-repo-url> digital-therapy
cd digital-therapy

# Set environment variables
export ANTHROPIC_API_KEY=<your-anthropic-api-key>
export JWT_SECRET=<your-jwt-secret>

# Build and start containers
docker compose up -d --build

# Monitor startup
docker compose logs -f
```

## 4. Verify Deployment

Check each endpoint in a browser:

| URL | Expected Result |
|-----|----------------|
| `http://<elastic-ip>:3000/` | Frontend application loads |
| `http://<elastic-ip>:8080/swagger-ui/index.html` | Swagger API documentation |
| `http://<elastic-ip>:8080/h2-console/login.jsp` | H2 database console |
| `http://<elastic-ip>:8080/actuator/health` | `{"status":"UP"}` |

## 5. Troubleshooting

```bash
# Check container status
docker compose ps

# View logs
docker compose logs backend
docker compose logs frontend

# Restart services
docker compose restart

# Rebuild from scratch
docker compose down
docker compose up -d --build
```

## Architecture

```
┌──────────────────────────────────────────────┐
│           AWS EC2 (t2.medium)                │
│           Elastic IP: <ip>                   │
│                                              │
│  ┌─────────────────┐  ┌──────────────────┐  │
│  │   Frontend       │  │   Backend        │  │
│  │   Nginx:Alpine   │  │   JRE 22:Alpine  │  │
│  │   Port 3000      │──│   Port 8080      │  │
│  │                  │  │                   │  │
│  │   Static React   │  │   Spring Boot    │  │
│  │   + API Proxy    │  │   + H2 Database  │  │
│  └─────────────────┘  └──────────────────┘  │
│                              │               │
│                        ┌─────┴─────┐         │
│                        │ Volume:   │         │
│                        │ app-data  │         │
│                        └───────────┘         │
└──────────────────────────────────────────────┘
         │                       │
    Port 3000               Port 8080
    (Frontend)           (API/Swagger/H2)
```
