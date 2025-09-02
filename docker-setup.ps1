#!/usr/bin/env pwsh
# University Course Management - Docker Setup Script for Windows
# This script helps you manage the Docker deployment

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("start", "stop", "restart", "logs", "clean", "build", "status", "help")]
    [string]$Action
)

$ErrorActionPreference = "Stop"

function Show-Help {
    Write-Host "🎓 University Course Management - Docker Setup" -ForegroundColor Green
    Write-Host ""
    Write-Host "Available commands:" -ForegroundColor Yellow
    Write-Host "  start   - Start all services (build if needed)"
    Write-Host "  stop    - Stop all services"
    Write-Host "  restart - Restart all services"
    Write-Host "  logs    - Show application logs"
    Write-Host "  clean   - Stop services and remove containers/volumes"
    Write-Host "  build   - Build/rebuild the application image"
    Write-Host "  status  - Show status of all services"
    Write-Host "  help    - Show this help message"
    Write-Host ""
    Write-Host "Examples:" -ForegroundColor Cyan
    Write-Host "  .\docker-setup.ps1 start"
    Write-Host "  .\docker-setup.ps1 logs"
    Write-Host "  .\docker-setup.ps1 clean"
}

function Test-DockerRunning {
    try {
        docker info | Out-Null
        return $true
    }
    catch {
        Write-Host "❌ Docker is not running. Please start Docker Desktop first." -ForegroundColor Red
        return $false
    }
}

function Start-Services {
    Write-Host "🚀 Starting University Course Management services..." -ForegroundColor Green
    
    if (-not (Test-DockerRunning)) { return }
    
    Write-Host "📦 Building and starting containers..." -ForegroundColor Yellow
    docker-compose up --build -d
    
    Write-Host "⏳ Waiting for services to be ready..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    Write-Host "✅ Services started successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "🌐 Application URLs:" -ForegroundColor Cyan
    Write-Host "  - API Base: http://localhost:8080"
    Write-Host "  - Health Check: http://localhost:8080/api/test/public"
    Write-Host "  - Database: localhost:3333"
    Write-Host ""
    Write-Host "👤 Default Admin Credentials:" -ForegroundColor Cyan
    Write-Host "  - Username: admin"
    Write-Host "  - Password: admin123"
}

function Stop-Services {
    Write-Host "🛑 Stopping University Course Management services..." -ForegroundColor Yellow
    
    if (-not (Test-DockerRunning)) { return }
    
    docker-compose down
    Write-Host "✅ Services stopped successfully!" -ForegroundColor Green
}

function Restart-Services {
    Write-Host "🔄 Restarting University Course Management services..." -ForegroundColor Yellow
    Stop-Services
    Start-Sleep -Seconds 5
    Start-Services
}

function Show-Logs {
    Write-Host "📋 Showing application logs..." -ForegroundColor Yellow
    
    if (-not (Test-DockerRunning)) { return }
    
    docker-compose logs -f university-app
}

function Clean-All {
    Write-Host "🧹 Cleaning up Docker resources..." -ForegroundColor Yellow
    
    if (-not (Test-DockerRunning)) { return }
    
    Write-Host "Stopping and removing containers..." -ForegroundColor Yellow
    docker-compose down -v --remove-orphans
    
    Write-Host "Removing application image..." -ForegroundColor Yellow
    docker rmi universitycoursemanagement_university-app -f 2>$null
    
    Write-Host "Pruning unused Docker resources..." -ForegroundColor Yellow
    docker system prune -f
    
    Write-Host "✅ Cleanup completed!" -ForegroundColor Green
}

function Build-Application {
    Write-Host "🏗️ Building application image..." -ForegroundColor Yellow
    
    if (-not (Test-DockerRunning)) { return }
    
    docker-compose build --no-cache university-app
    Write-Host "✅ Build completed!" -ForegroundColor Green
}

function Show-Status {
    Write-Host "📊 Service Status:" -ForegroundColor Yellow
    
    if (-not (Test-DockerRunning)) { return }
    
    docker-compose ps
    
    Write-Host ""
    Write-Host "🔍 Quick Health Check:" -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/api/test/public" -TimeoutSec 5
        Write-Host "✅ Application is responding: $($response.StatusCode)" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ Application is not responding" -ForegroundColor Red
    }
}

# Main execution
switch ($Action.ToLower()) {
    "start"   { Start-Services }
    "stop"    { Stop-Services }
    "restart" { Restart-Services }
    "logs"    { Show-Logs }
    "clean"   { Clean-All }
    "build"   { Build-Application }
    "status"  { Show-Status }
    "help"    { Show-Help }
}
