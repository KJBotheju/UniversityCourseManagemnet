# Authentication API Documentation

## Overview
The University Course Management System now includes JWT-based authentication with login and signup functionality.

## Authentication Endpoints

### 1. Signup (Register New User)
**POST** `/api/auth/signup`

**Request Body:**
```json
{
    "username": "student1",
    "email": "student1@university.edu",
    "password": "password123",
    "role": "STUDENT"
}
```

**Response:**
```json
{
    "message": "User registered successfully!",
    "type": "Bearer"
}
```

**Available Roles:**
- `STUDENT` (default)
- `INSTRUCTOR` 
- `ADMIN`

### 2. Login
**POST** `/api/auth/login`

**Request Body:**
```json
{
    "username": "student1",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "username": "student1",
    "email": "student1@university.edu",
    "role": "STUDENT"
}
```

### 3. Logout
**POST** `/api/auth/logout`

**Response:**
```json
{
    "message": "User logged out successfully!",
    "type": "Bearer"
}
```

## Test Endpoints

### Public Endpoint (No Authentication Required)
**GET** `/api/test/public`

**Response:**
```json
{
    "message": "This is a public endpoint - no authentication required"
}
```

### Protected Endpoint (Authentication Required)
**GET** `/api/test/user`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Response:**
```json
{
    "message": "Hello authenticated user: student1",
    "authorities": "[ROLE_STUDENT]"
}
```

## API Access Permissions

### Courses (`/api/courses/**`)
- **Access:** STUDENT, INSTRUCTOR, ADMIN
- **Operations:** View courses, enroll (students), manage (instructors/admins)

### Students (`/api/students/**`)
- **Access:** INSTRUCTOR, ADMIN only
- **Operations:** Manage student records

### Enrollments (`/api/enrollments/**`)
- **Access:** STUDENT, INSTRUCTOR, ADMIN
- **Operations:** View enrollments, manage grades

## How to Use

1. **Register a new user** using the signup endpoint
2. **Login** to get a JWT token
3. **Include the token** in the Authorization header for protected endpoints:
   ```
   Authorization: Bearer your_jwt_token_here
   ```

## Testing with cURL

### Signup:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
-H "Content-Type: application/json" \
-d '{
    "username": "testuser",
    "email": "test@example.com", 
    "password": "password123",
    "role": "STUDENT"
}'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
    "username": "testuser",
    "password": "password123"
}'
```

### Access Protected Endpoint:
```bash
curl -X GET http://localhost:8080/api/test/user \
-H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## Database Changes

The following changes were made to support authentication:

1. **New `users` table** created automatically with:
   - `id`, `username`, `email`, `password`, `role`, `status`
   
2. **Status field added** to existing tables:
   - `courses`, `students`, `enrollments` now have `status` field
   - Supports soft delete functionality (ACTIVE/DELETED)

3. **Soft Delete Implementation:**
   - Delete operations now set status to DELETED instead of removing records
   - All list/get operations only return ACTIVE records
