# 🎓 University Course Management System

A comprehensive REST API application built with Spring Boot for managing university courses, students, enrollments, and authentication with JWT security.

### Core Functionality
- **Student Management**: Create, read, update, and soft delete student records
- **Course Management**: Manage university courses with capacity limits
- **Enrollment System**: Student enrollment in courses with GPA calculation
- **Soft Delete**: All entities support soft delete (status-based deletion)

### Authentication & Security
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Three user roles (STUDENT, INSTRUCTOR, ADMIN)
- **Password Encryption**: BCrypt password hashing
- **CORS Support**: Cross-origin resource sharing enabled

### Additional Features
- **Pagination**: Paginated API responses
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Structured error responses
- **Health Checks**: Built-in health monitoring endpoints

## 🛠 Tech Stack

- **Backend**: Java 17, Spring Boot 3.5.5
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Documentation**: Comprehensive README and API docs

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0
- Docker & Docker Compose (optional)
- Git

## 🚀 Installation & Setup

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/KJBotheju/UniversityCourseManagemnet.git
   cd UniversityCourseManagement
   ```

2. **Setup MySQL Database**
   ```sql
   CREATE DATABASE universityCourseManagement;
   ```

3. **Configure Application Properties**
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3333/universityCourseManagement?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=1234
   
   # JWT Configuration
   app.jwtSecret=UniversityCourseManagementSecretKeyForJWTSigningMustBe256BitsOrMore
   app.jwtExpirationMs=86400000
   ```

4. **Build and Run**
   ```bash
   # Build the project
   mvn clean compile
   
   # Run the application
   mvn spring-boot:run
   ```

5. **Access the Application**
   - API Base URL: `http://localhost:8080`

## 🐳 Docker Deployment

### Quick Start with Docker

```bash
# Using setup scripts (Windows)
docker-setup.bat start

# Using setup scripts (Linux/Mac)
chmod +x docker-setup.sh
./docker-setup.sh start

# Or manually with Docker Compose
docker-compose up --build -d
```

### Docker Services
- **Spring Boot App**: `http://localhost:8080`
- **MySQL Database**: `localhost:3333`

For detailed Docker setup instructions, see [DOCKER_README.md](DOCKER_README.md).

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/api/auth/signup` | Register new user | Public |
| POST | `/api/auth/login` | User login | Public |
| POST | `/api/auth/logout` | User logout | Public |

### Course Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/courses` | List all courses (paginated) | STUDENT, INSTRUCTOR, ADMIN |
| POST | `/api/courses` | Create new course | INSTRUCTOR, ADMIN |
| GET | `/api/courses/{id}` | Get course by ID | STUDENT, INSTRUCTOR, ADMIN |
| PUT | `/api/courses/{id}` | Update course | INSTRUCTOR, ADMIN |
| DELETE | `/api/courses/{id}` | Soft delete course | INSTRUCTOR, ADMIN |

### Student Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/students` | List all students (paginated) | INSTRUCTOR, ADMIN |
| POST | `/api/students` | Create new student | INSTRUCTOR, ADMIN |
| GET | `/api/students/{id}` | Get student by ID | INSTRUCTOR, ADMIN |
| PUT | `/api/students/{id}` | Update student | INSTRUCTOR, ADMIN |
| DELETE | `/api/students/{id}` | Soft delete student | INSTRUCTOR, ADMIN |

### Enrollment Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/api/enrollments/enroll` | Enroll student in course | STUDENT, INSTRUCTOR, ADMIN |
| PATCH | `/api/enrollments/{id}/grade` | Assign grade | INSTRUCTOR, ADMIN |
| GET | `/api/enrollments/by-student/{id}` | Get enrollments by student | STUDENT, INSTRUCTOR, ADMIN |
| GET | `/api/enrollments/by-course/{id}` | Get enrollments by course | INSTRUCTOR, ADMIN |
| DELETE | `/api/enrollments/{id}` | Drop enrollment | STUDENT, INSTRUCTOR, ADMIN |
| GET | `/api/enrollments/gpa/{studentId}` | Calculate student GPA | STUDENT, INSTRUCTOR, ADMIN |

## 🔐 Authentication

The system uses JWT (JSON Web Token) based authentication with role-based access control.

### User Roles
- **STUDENT**: Can view courses, manage own enrollments
- **INSTRUCTOR**: Can manage courses, students, and enrollments
- **ADMIN**: Full system access

### Authentication Flow

1. **Register** a new user:
   ```bash
   curl -X POST http://localhost:8080/api/auth/signup \
   -H "Content-Type: application/json" \
   -d '{
     "username": "student1",
     "email": "student1@university.edu",
     "password": "password123",
     "role": "STUDENT"
   }'
   ```

2. **Login** to get JWT token:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{
     "username": "student1",
     "password": "password123"
   }'
   ```

3. **Use token** in subsequent requests:
   ```bash
   curl -X GET http://localhost:8080/api/courses \
   -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
   ```

For detailed authentication documentation, see [AUTH_README.md](AUTH_README.md).

## 🗄 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') DEFAULT 'STUDENT',
    status ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE'
);
```

### Courses Table
```sql
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(16) UNIQUE NOT NULL,
    title VARCHAR(128) NOT NULL,
    credits INT DEFAULT 3,
    capacity INT DEFAULT 50,
    status ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE'
);
```

### Students Table
```sql
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    index_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,
    user_id BIGINT,
    status ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Enrollments Table
```sql
CREATE TABLE enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    grade ENUM('A+', 'A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'C-', 'D+', 'D', 'F'),
    status ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE',
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE KEY uk_student_course (student_id, course_id)
);

```
## 🔧 Usage Examples

### Creating a Course
```bash
curl -X POST http://localhost:8080/api/courses \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d '{
  "code": "CS101",
  "title": "Introduction to Computer Science",
  "credits": 3,
  "capacity": 30
}'
```

### Enrolling a Student
```bash
curl -X POST http://localhost:8080/api/enrollments/enroll \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d "studentId=1&courseId=1"
```

### Calculating GPA
```bash
curl -X GET http://localhost:8080/api/enrollments/gpa/1 \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Pagination Example
```bash
curl -X GET "http://localhost:8080/api/courses?page=0&size=10&sort=title,asc" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🚦 Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=CourseServiceTest
```

## 🔧 Configuration

### Application Properties
Key configuration properties you can customize:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3333/universityCourseManagement
spring.datasource.username=root
spring.datasource.password=1234

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT Configuration
app.jwtSecret=YourSecretKeyHere
app.jwtExpirationMs=86400000

# CORS Configuration (handled in WebCorsConfig.java)
# Allows requests from http://localhost:3000 by default
```

### Environment Variables
You can override properties using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/your-database
export SPRING_DATASOURCE_USERNAME=your-username
export SPRING_DATASOURCE_PASSWORD=your-password
export APP_JWT_SECRET=your-jwt-secret
```

## 🧪 Testing the API

### Using cURL

1. **Health Check**:
   ```bash
   curl http://localhost:8080/api/test/public
   ```

2. **Create User and Login**:
   ```bash
   # Signup
   curl -X POST http://localhost:8080/api/auth/signup \
   -H "Content-Type: application/json" \
   -d '{"username":"testuser","email":"test@example.com","password":"password123","role":"STUDENT"}'
   
   # Login (save the token from response)
   curl -X POST http://localhost:8080/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{"username":"testuser","password":"password123"}'
   ```

3. **Test Protected Endpoints**:
   ```bash
   curl -X GET http://localhost:8080/api/test/user \
   -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
   ```

### Using Postman

1. Import the API collection (create one from the endpoints listed above)
2. Set up environment variables for base URL and JWT token
3. Use the authentication endpoints to get a token
4. Add the token to the Authorization header for protected endpoints

## 🚀 Deployment

### Production Deployment

1. **Build for Production**:
   ```bash
   mvn clean package -Dspring.profiles.active=prod
   ```

2. **Run with Production Profile**:
   ```bash
   java -jar -Dspring.profiles.active=prod target/UniversityCourseManagement-0.0.1-SNAPSHOT.jar
   ```

3. **Docker Production Deployment**:
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

### Cloud Deployment

The application is ready for deployment on:
- AWS (EC2, ECS, Elastic Beanstalk)
- Google Cloud Platform (GKE, App Engine)
- Azure (Container Instances, App Service)
- Heroku
- DigitalOcean

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR
- Use meaningful commit messages

## 📝 API Response Examples

### Successful Response
```json
{
  "id": 1,
  "code": "CS101",
  "title": "Introduction to Computer Science",
  "credits": 3,
  "capacity": 30,
  "status": "ACTIVE"
}
```

### Error Response
```json
{
  "message": "Course not found: 999",
  "status": 404,
  "timestamp": "2025-08-28T18:30:00.000+00:00"
}
```

### Paginated Response
```json
{
  "content": [...],
  "pageable": {
    "sort": {...},
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3
}
```

## 🐛 Troubleshooting

### Common Issues

1. **JWT Token Issues**:
   - Ensure JWT secret is at least 256 bits (32 characters)
   - Check token expiration time
   - Verify Authorization header format: `Bearer <token>`

2. **Database Connection Issues**:
   - Verify MySQL is running
   - Check connection URL, username, and password
   - Ensure database exists or `createDatabaseIfNotExist=true` is set

3. **CORS Issues**:
   - Check allowed origins in `WebCorsConfig.java`
   - Verify preflight requests are handled
   - Ensure proper headers are included

4. **Docker Issues**:
   - Check if ports are already in use
   - Verify Docker daemon is running
   - Check container logs: `docker-compose logs`

## 📊 Performance Considerations

- **Database Indexing**: Key fields are indexed for optimal query performance
- **Pagination**: Large datasets are paginated to prevent memory issues
- **Soft Delete**: Maintains data integrity while allowing "deletion"
- **Connection Pooling**: HikariCP for efficient database connections
- **JWT Stateless**: No server-side session storage required

## 🔒 Security Features

- **Password Hashing**: BCrypt with configurable strength
- **JWT Security**: Secure token generation and validation
- **CORS Protection**: Configured for cross-origin requests
- **Input Validation**: Comprehensive request validation
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **Authentication Required**: All business endpoints require authentication

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the documentation files:
  - [AUTH_README.md](AUTH_README.md) - Authentication details
  - [DOCKER_README.md](DOCKER_README.md) - Docker deployment guide

## 👨‍💻 Author

**KJBotheju**
- GitHub: [@KJBotheju](https://github.com/KJBotheju)
- Repository: [UniversityCourseManagemnet](https://github.com/KJBotheju/UniversityCourseManagemnet)
