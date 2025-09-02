@echo off
REM University Course Management - Docker Setup Script for Windows
REM This batch file provides easy access to Docker commands

if "%1"=="" goto help

if "%1"=="start" goto start
if "%1"=="stop" goto stop
if "%1"=="restart" goto restart
if "%1"=="logs" goto logs
if "%1"=="clean" goto clean
if "%1"=="build" goto build
if "%1"=="status" goto status
if "%1"=="help" goto help

echo Invalid command: %1
goto help

:start
echo 🚀 Starting University Course Management services...
docker-compose up --build -d
echo ✅ Services started! Visit: http://localhost:8080/api/test/public
goto end

:stop
echo 🛑 Stopping services...
docker-compose down
echo ✅ Services stopped!
goto end

:restart
echo 🔄 Restarting services...
docker-compose down
timeout /t 5 /nobreak >nul
docker-compose up --build -d
echo ✅ Services restarted!
goto end

:logs
echo 📋 Showing logs...
docker-compose logs -f university-app
goto end

:clean
echo 🧹 Cleaning up...
docker-compose down -v --remove-orphans
docker rmi universitycoursemanagement_university-app -f 2>nul
docker system prune -f
echo ✅ Cleanup completed!
goto end

:build
echo 🏗️ Building application...
docker-compose build --no-cache university-app
echo ✅ Build completed!
goto end

:status
echo 📊 Service Status:
docker-compose ps
echo.
echo 🔍 Testing application...
curl -s http://localhost:8080/api/test/public && echo ✅ App is running! || echo ❌ App is not responding
goto end

:help
echo.
echo 🎓 University Course Management - Docker Setup
echo.
echo Available commands:
echo   start   - Start all services (build if needed)
echo   stop    - Stop all services  
echo   restart - Restart all services
echo   logs    - Show application logs
echo   clean   - Stop services and remove containers/volumes
echo   build   - Build/rebuild the application image
echo   status  - Show status of all services
echo   help    - Show this help message
echo.
echo Examples:
echo   docker-setup.bat start
echo   docker-setup.bat logs
echo   docker-setup.bat clean
echo.
echo 🌐 URLs after starting:
echo   - API: http://localhost:8080
echo   - Health: http://localhost:8080/api/test/public  
echo   - Database: localhost:3333
echo.
echo 👤 Default admin: admin/admin123

:end
