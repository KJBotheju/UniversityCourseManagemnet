# 🐳 Docker Deployment Guide

This guide provides comprehensive instructions for deploying the University Course Management System using Docker.
## 🚀 Quick Start

### Windows (Batch Script)
```cmd
# Start all services
docker-setup.bat start

# Check status
docker-setup.bat status

# View logs
docker-setup.bat logs

# Stop services
docker-setup.bat stop
```

### Windows (PowerShell)
```powershell
# Start all services
.\docker-setup.ps1 start

# Check status
.\docker-setup.ps1 status

# View logs
.\docker-setup.ps1 logs

# Stop services
.\docker-setup.ps1 stop
```

### Manual Docker Compose
```bash
# Start services
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 📋 Prerequisites

- **Docker Desktop** (Windows/Mac) or **Docker Engine** (Linux)
- **Docker Compose** v2.0 or higher
- **4GB RAM** minimum (8GB recommended)
- **5GB free disk space** for images and volumes

### Verify Prerequisites
```bash
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version

# Verify Docker is running
docker info
```

## 🔧 Docker Configuration

### Services Overview

| Service | Container Name | Port | Description |
|---------|---------------|------|-------------|
| `university-app` | `university-course-app` | 8080 | Spring Boot application |
| `mysql-db` | `university-mysql` | 3333 | MySQL 8.0 database |

### Docker Compose Structure

```yaml
version: '3.8'
services:
  mysql-db:
    image: mysql:8.0
    ports: ["3333:3306"]
    volumes: [mysql_data:/var/lib/mysql]
    
  university-app:
    build: .
    ports: ["8080:8080"]
    depends_on: [mysql-db]
```

## 🚀 Deployment Options

### 1. Development Deployment (Default)

Uses the standard `Dockerfile` with full development features:

```bash
docker-compose up --build -d
```

**Features:**
- Full Maven build inside container
- Development-friendly configuration
- All debugging tools included

### 2. Production Deployment (Optimized)

Uses multi-stage `Dockerfile.optimized` for production:

```bash
docker-compose -f docker-compose.prod.yml up --build -d
```

Create `docker-compose.prod.yml`:
```yaml
version: '3.8'
services:
  university-app:
    build:
      context: .
      dockerfile: Dockerfile.optimized
```

**Features:**
- Multi-stage build (smaller image)
- JVM optimizations for containers
- Security hardened
- Runtime-only dependencies

### 3. External Database Deployment

For using external MySQL database:

```bash
# Remove MySQL service from compose file
# Set database environment variables
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/universityCourseManagement
docker-compose up university-app
```

## 🌍 Environment Variables

### Database Configuration
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/universityCourseManagement
SPRING_DATASOURCE_USERNAME=appuser
SPRING_DATASOURCE_PASSWORD=apppassword
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
```

### JPA/Hibernate Configuration
```bash
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQL8Dialect
```

### JWT Security Configuration
```bash
APP_JWT_SECRET=UniversityCourseManagementSecretKeyForJWTSigningMustBe256BitsOrMore
APP_JWT_EXPIRATION_MS=86400000
```

### Server Configuration
```bash
SERVER_PORT=8080
```

### MySQL Database Configuration
```bash
MYSQL_ROOT_PASSWORD=1234
MYSQL_DATABASE=universityCourseManagement
MYSQL_USER=appuser
MYSQL_PASSWORD=apppassword
```

## 💾 Volumes and Data Persistence

### Named Volumes

```yaml
volumes:
  mysql_data:       # Persists MySQL database data
    driver: local
  app_logs:         # Persists application logs
    driver: local
```

### Volume Management Commands

```bash
# List all volumes
docker volume ls

# Inspect MySQL data volume
docker volume inspect universitycoursemanagement_mysql_data

# Backup MySQL data
docker run --rm -v universitycoursemanagement_mysql_data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-backup.tar.gz -C /data .

# Restore MySQL data
docker run --rm -v universitycoursemanagement_mysql_data:/data -v $(pwd):/backup alpine tar xzf /backup/mysql-backup.tar.gz -C /data

# Remove all volumes (⚠️ DATA LOSS)
docker-compose down -v
```

## 🌐 Networking

### Custom Network

```yaml
networks:
  university-network:
    driver: bridge
```

**Benefits:**
- Isolated network for services
- Service discovery by name
- Better security

### Port Mapping

| Internal Port | External Port | Service |
|--------------|---------------|---------|
| 3306 | 3333 | MySQL Database |
| 8080 | 8080 | Spring Boot App |

### Network Commands

```bash
# List Docker networks
docker network ls

# Inspect custom network
docker network inspect universitycoursemanagement_university-network

# Test connectivity between containers
docker exec university-course-app ping university-mysql
```


### Manual Health Check Commands

```bash
# Check application health
curl http://localhost:8080/api/test/public

# Check database health
docker exec university-mysql mysqladmin ping

# Check container health status
docker-compose ps
```

## 🔍 Troubleshooting

### Common Issues and Solutions

#### 1. Port Already in Use
```bash
# Error: Port 8080 is already allocated
# Solution: Stop conflicting services or change port

# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID <PID> /F

# Or change port in docker-compose.yml
ports:
  - "8081:8080"  # Use port 8081 instead
```

#### 2. Database Connection Failed
```bash
# Check if MySQL is healthy
docker-compose logs mysql-db

# Check network connectivity
docker exec university-course-app ping mysql-db

# Verify environment variables
docker exec university-course-app env | grep SPRING_DATASOURCE
```

#### 3. Application Won't Start
```bash
# Check application logs
docker-compose logs university-app

# Check if database is ready
docker-compose ps

# Restart with fresh build
docker-compose down
docker-compose up --build
```

#### 4. Out of Memory Errors
```bash
# Increase Docker memory limit (Docker Desktop)
# Settings > Resources > Memory > 4GB+

# Or use optimized Dockerfile with memory settings
ENV JAVA_OPTS="-Xmx1g -XX:MaxRAMPercentage=75.0"
```

#### 5. Volume Permission Issues
```bash
# Fix volume permissions (Linux/Mac)
sudo chown -R $USER:$USER ./docker/mysql/init

# Or recreate volumes
docker-compose down -v
docker-compose up --build
```

### Debug Commands

```bash
# Enter application container
docker exec -it university-course-app sh

# Enter MySQL container
docker exec -it university-mysql mysql -u root -p

# Check container resource usage
docker stats

# View detailed container info
docker inspect university-course-app

# Check Docker system info
docker system df
docker system info
```

## 🚀 Production Deployment

### 1. Environment Setup

Create `.env.prod` file:
```bash
# Database
MYSQL_ROOT_PASSWORD=your-secure-password
MYSQL_PASSWORD=your-app-password
DB_HOST=your-db-host

# JWT
APP_JWT_SECRET=your-256-bit-secret-key
APP_JWT_EXPIRATION_MS=86400000

# Application
SPRING_PROFILES_ACTIVE=prod
```

### 2. Production Docker Compose

Create `docker-compose.prod.yml`:
```yaml
version: '3.8'
services:
  university-app:
    build:
      context: .
      dockerfile: Dockerfile.optimized
    env_file:
      - .env.prod
    restart: always
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

### 3. SSL/HTTPS Setup

```bash
# Use reverse proxy (nginx/traefik) for SSL termination
# Or configure Spring Boot with SSL certificates

# Example nginx configuration
server {
    listen 443 ssl;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 4. Monitoring and Logging

```yaml
# Add monitoring tools
services:
  # ... existing services ...
  
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
      
  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
```

### 5. Backup Strategy

```bash
#!/bin/bash
# backup-script.sh

# Backup database
docker exec university-mysql mysqldump -u root -p$MYSQL_ROOT_PASSWORD universityCourseManagement > backup-$(date +%Y%m%d).sql

# Backup volumes
docker run --rm -v universitycoursemanagement_mysql_data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-data-$(date +%Y%m%d).tar.gz -C /data .
```

### 6. Security Hardening

```yaml
# Security improvements
services:
  university-app:
    security_opt:
      - no-new-privileges:true
    cap_drop:
      - ALL
    cap_add:
      - NET_BIND_SERVICE
    read_only: true
    tmpfs:
      - /tmp:noexec,nosuid,size=1g
```

## 📊 Performance Tuning

### JVM Optimization (Dockerfile.optimized)
```bash
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UnlockExperimentalVMOptions \
               -XX:+UseJVMCICompiler"
```

### MySQL Optimization
```yaml
mysql-db:
  command: >
    --innodb-buffer-pool-size=1G
    --innodb-log-file-size=256M
    --max-connections=200
    --query-cache-size=64M
```

### Resource Limits
```yaml
services:
  university-app:
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 2G
        reservations:
          cpus: '0.5'
          memory: 1G
```

## 🔄 CI/CD Integration

### GitHub Actions Example
```yaml
name: Docker Build and Deploy
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build and Deploy
        run: |
          docker-compose -f docker-compose.prod.yml up --build -d
```

## 📈 Monitoring URLs

After starting with Docker:

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/api/test/public
- **API Documentation**: http://localhost:8080/swagger-ui.html (if configured)
- **Database**: localhost:3333 (use MySQL client)