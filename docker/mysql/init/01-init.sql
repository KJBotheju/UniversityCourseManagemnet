-- Initialize University Course Management Database
-- This script runs when MySQL container starts for the first time

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS universityCourseManagement;

-- Switch to the database
USE universityCourseManagement;

-- Grant privileges to application user
GRANT ALL PRIVILEGES ON universityCourseManagement.* TO 'appuser'@'%';
FLUSH PRIVILEGES;

-- Create an admin user for initial setup (password: admin123)
-- This will be created when the application starts via JPA/Hibernate
INSERT IGNORE INTO users (username, email, password, role, status) VALUES 
('admin', 'admin@university.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5lE2UYnYx3SnBrqGp6', 'ADMIN', 'ACTIVE');

-- Note: The password is BCrypt encoded for 'admin123'
-- Tables will be auto-created by Hibernate when the Spring Boot app starts
