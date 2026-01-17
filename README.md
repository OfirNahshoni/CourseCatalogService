# Course Catalog Service

A RESTful API service built with Spring Boot and Kotlin for managing courses and instructors. This service provides endpoints to create, retrieve, update, and delete courses and instructors with proper validation and error handling.

## 🚀 Features

- **Course Management**: Create, read, update, and delete courses
- **Instructor Management**: Create, read, and delete instructors
- **Database Integration**: PostgreSQL database with JPA/Hibernate
- **Input Validation**: Jakarta Bean Validation for request validation
- **Global Exception Handling**: Centralized error handling with proper HTTP status codes
- **Testing**: Unit and integration tests
- **Docker Support**: Docker Compose configuration for PostgreSQL

## 🛠️ Technology Stack

- **Language**: Kotlin 2.2.21
- **Framework**: Spring Boot 4.0.1
- **Java Version**: 21
- **Database**: PostgreSQL 16.3
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockK, SpringMockK, WebFlux Test

## 📋 Prerequisites

- Java 21 or higher
- Gradle (or use the included Gradle Wrapper)
- Docker and Docker Compose (for running PostgreSQL)

## 🏗️ Project Structure

```
src/
├── main/
│   ├── kotlin/com/kotlinspring/
│   │   ├── controller/          # REST controllers
│   │   │   ├── CourseController.kt
│   │   │   └── InstructorController.kt
│   │   ├── service/             # Business logic
│   │   │   ├── CourseService.kt
│   │   │   └── InstructorService.kt
│   │   ├── repository/          # Data access layer
│   │   │   ├── CourseRepository.kt
│   │   │   └── InstructorRepository.kt
│   │   ├── entity/              # JPA entities
│   │   │   ├── Course.kt
│   │   │   └── Instructor.kt
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── CourseDTO.kt
│   │   │   └── InstructorDTO.kt
│   │   ├── exception/           # Custom exceptions
│   │   │   ├── CourseNotFoundException.kt
│   │   │   ├── InstructorNotFoundException.kt
│   │   │   └── InstructorNotValidException.kt
│   │   └── exceptionhandler/    # Global exception handler
│   │       └── GlobalErrorHandler.kt
│   └── resources/
│       └── application.yml       # Application configuration
└── test/
    ├── unit/                    # Unit tests
    └── intg/                    # Integration tests
```

## 🚀 Getting Started

### 1. Start PostgreSQL Database

Using Docker Compose:

```bash
docker-compose up -d
```

This will start a PostgreSQL container on port `5433` with:
- Database: `courses_db`
- Username: `udemy_training_user`
- Password: `ofir221`

### 2. Build the Project

```bash
./gradlew build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on the default Spring Boot port (usually `8080`).

## 📡 API Endpoints

### Course Endpoints

#### Create a Course
```http
POST /v1/courses
Content-Type: application/json

{
  "name": "Kotlin Programming",
  "category": "Programming",
  "instructorId": 1
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "name": "Kotlin Programming",
  "category": "Programming",
  "instructorId": 1
}
```

#### Retrieve All Courses
```http
GET /v1/courses?course_name=Kotlin
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "Kotlin Programming",
    "category": "Programming",
    "instructorId": 1
  }
]
```

#### Update a Course
```http
PUT /v1/courses/{courseId}
Content-Type: application/json

{
  "name": "Advanced Kotlin",
  "category": "Programming"
}
```

**Response**: `200 OK`

#### Delete a Course
```http
DELETE /v1/courses/{courseId}
```

**Response**: `204 No Content`

### Instructor Endpoints

#### Create an Instructor
```http
POST /v1/instructors
Content-Type: application/json

{
  "name": "John Doe"
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "name": "John Doe"
}
```

#### Retrieve All Instructors
```http
GET /v1/instructors?instructor_name=John
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe"
  }
]
```

#### Delete an Instructor
```http
DELETE /v1/instructors/{instructorId}
```

**Response**: `204 No Content`

## 🔍 Data Models

### Course Entity
- `id`: Integer (auto-generated)
- `name`: String (required, not blank)
- `category`: String (required, not blank)
- `instructor`: Many-to-One relationship with Instructor

### Instructor Entity
- `id`: Integer (auto-generated)
- `name`: String (required, not blank)
- `courses`: One-to-Many relationship with Course

## ⚙️ Configuration

The application configuration is in `src/main/resources/application.yml`:

- **Database**: PostgreSQL on `localhost:5433`
- **JPA**: Hibernate with `create-drop` DDL mode (⚠️ **Note**: This recreates the database on startup - change for production!)
- **Profiles**: Supports `prod`, `non-prod`, and `test` profiles

### Important Configuration Notes

⚠️ **Warning**: The current configuration uses `ddl-auto: create-drop`, which will drop and recreate the database schema on every application restart. This should be changed to `update` or `validate` for production environments.

## 🧪 Testing

The project includes both unit and integration tests:

### Run All Tests
```bash
./gradlew test
```

### Run Unit Tests Only
```bash
./gradlew test --tests "*UnitTest"
```

### Run Integration Tests Only
```bash
./gradlew test --tests "*IntgTest"
```

## 🐛 Error Handling

The application includes a global exception handler that provides consistent error responses:

- **Validation Errors**: Returns `400 Bad Request` with a list of validation errors
- **Not Found Errors**: Returns `400 Bad Request` with error message
- **General Exceptions**: Returns `500 Internal Server Error` with error message

### Custom Exceptions
- `CourseNotFoundException`: Thrown when a course is not found
- `InstructorNotFoundException`: Thrown when an instructor is not found
- `InstructorNotValidException`: Thrown when an invalid instructor ID is provided

## 📦 Dependencies

Key dependencies include:
- Spring Boot Web
- Spring Boot Data JPA
- Spring Boot Validation
- PostgreSQL Driver
- Jackson Kotlin Module
- Kotlin Logging
- MockK and SpringMockK (for testing)
- WebFlux Test (for testing)

## 🔧 Development

### Building
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Cleaning Build Artifacts
```bash
./gradlew clean
```

## 📝 Notes

- The application uses Kotlin data classes for entities and DTOs
- JPA entities use lazy loading for relationships
- The service layer handles business logic and DTO-to-Entity conversions
- All endpoints are versioned under `/v1/`

## 🚧 TODO

- [ ] Replace logging with Kafka for course creation events (currently using logger.info)
- [ ] Change `ddl-auto` from `create-drop` to `update` or `validate` for production
- [ ] Add more comprehensive error handling
- [ ] Add API documentation (Swagger/OpenAPI)

## 📄 License

This is a training/demo project.
