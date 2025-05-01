# Book Store API

A Spring Boot application for book store management with CRUD operations for authors and books, including authentication and authorization.

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
  - [Author](#author)
  - [Book](#book)
  - [User](#user)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Authors](#authors)
  - [Books](#books)
  - [Users](#users)
- [Setup and Run](#setup-and-run)
  - [Prerequisites](#prerequisites)
  - [Installation Steps](#installation-steps)
  - [Using Environment Variables](#using-environment-variables)
  - [Environment Profiles](#environment-profiles)
  - [H2 Database Console](#h2-database-console)
  - [Preloaded Data](#preloaded-data)
  - [Setting Up an Admin User](#setting-up-an-admin-user)
- [Troubleshooting](#troubleshooting)
  - [Common Issues](#common-issues)
- [Technologies](#technologies)
- [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
- [Environment Variables](#environment-variables)
- [Logging](#logging)

## Features

- User authentication with JWT (Bearer token, 15 minutes expiration)
- Sign up, sign in, and sign out functionality
- Book and Author management with proper relationships
- H2 in-memory database for development
- RESTful API with proper error handling
- Role-based access control (USER and ADMIN roles)
- Cascading delete (when an author is deleted, all their books are also deleted)

## Project Structure

```
book-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bookstore/
│   │   │           ├── BookStoreApplication.java    # Main application class
│   │   │           ├── config/                      # Configuration classes
│   │   │           │   └── DataLoader.java          # Initial data loader
│   │   │           ├── controller/                  # REST controllers
│   │   │           │   ├── AuthController.java      # Authentication endpoints
│   │   │           │   ├── AuthorController.java    # Author CRUD endpoints
│   │   │           │   ├── BookController.java      # Book CRUD endpoints
│   │   │           │   └── UserController.java      # User management endpoints
│   │   │           ├── dto/                         # Data Transfer Objects
│   │   │           │   ├── AuthorDto.java           # Author request/response
│   │   │           │   ├── BookDto.java             # Book request/response
│   │   │           │   ├── JwtResponse.java         # JWT authentication response
│   │   │           │   ├── LoginRequest.java        # Login request
│   │   │           │   ├── MessageResponse.java     # Generic message response
│   │   │           │   ├── SignupRequest.java       # Signup request
│   │   │           │   └── UserDto.java             # User data transfer object
│   │   │           ├── exception/                   # Exception handlers
│   │   │           │   └── GlobalExceptionHandler.java
│   │   │           ├── model/                       # Entity models
│   │   │           │   ├── Author.java              # Author entity
│   │   │           │   ├── Book.java                # Book entity
│   │   │           │   ├── Role.java                # User role enum
│   │   │           │   └── User.java                # User entity
│   │   │           ├── repository/                  # Data repositories
│   │   │           │   ├── AuthorRepository.java    # Author data access
│   │   │           │   ├── BookRepository.java      # Book data access
│   │   │           │   └── UserRepository.java      # User data access
│   │   │           ├── security/                    # Security configuration
│   │   │           │   ├── AuthEntryPointJwt.java   # JWT authentication entry point
│   │   │           │   ├── AuthTokenFilter.java     # JWT token filter
│   │   │           │   ├── JwtUtils.java            # JWT utility methods
│   │   │           │   ├── UserDetailsImpl.java     # UserDetails implementation
│   │   │           │   ├── UserDetailsServiceImpl.java # UserDetailsService implementation
│   │   │           │   └── WebSecurityConfig.java   # Security configuration
│   │   │           └── service/                     # Business logic
│   │   │               ├── AuthorService.java       # Author service
│   │   │               ├── AuthService.java         # Authentication service
│   │   │               ├── BookService.java         # Book service
│   │   │               └── UserService.java         # User service
│   │   └── resources/
│   │       └── application.yml                      # Application configuration
│   └── test/                                        # Unit and integration tests
├── build.gradle                                     # Gradle build config
├── set_db_env.sh                                    # Environment variables script
├── settings.gradle                                  # Gradle settings
└── README.md                                        # Project documentation
```

## Database Schema

### Author
- **id**: Long (Primary Key)
- **name**: String (Not Null)
- **biography**: String
- One-to-many relationship with Books

### Book
- **id**: Long (Primary Key)
- **title**: String (Not Null)
- **isbn**: String
- **price**: BigDecimal (Not Null)
- **description**: String
- **author_id**: Long (Foreign Key)
- Many-to-one relationship with Author

### User
- **id**: Long (Primary Key)
- **username**: String (Not Null, Unique)
- **email**: String (Not Null, Unique)
- **password**: String (Encrypted, Not Null)
- **role**: Enum (USER, ADMIN)

## API Endpoints

### Authentication

- `POST http://localhost:8080/api/auth/signup`: Register a new user
  - Request Body:
    ```json
    {
      "username": "newuser",
      "email": "user@example.com",
      "password": "password123"
    }
    ```
  - Response (200 OK):
    ```json
    {
      "message": "User registered successfully!"
    }
    ```

- `POST http://localhost:8080/api/auth/signin`: Login and get JWT token
  - Request Body:
    ```json
    {
      "username": "newuser",
      "password": "password123"
    }
    ```
  - Response (200 OK):
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9...",
      "type": "Bearer",
      "id": 1,
      "username": "newuser",
      "email": "user@example.com",
      "role": "ROLE_USER"
    }
    ```

- `POST http://localhost:8080/api/auth/signout`: Logout (client-side)
  - Response (200 OK):
    ```json
    {
      "message": "User logged out successfully!"
    }
    ```

### Authors

- `GET http://localhost:8080/api/authors`: Get all authors
  - Response (200 OK):
    ```json
    [
      {
        "id": 1,
        "name": "J.K. Rowling",
        "biography": "British author known for writing the Harry Potter series"
      },
      {
        "id": 2,
        "name": "George Orwell",
        "biography": "English novelist, essayist, and critic"
      }
    ]
    ```

- `GET http://localhost:8080/api/authors/{id}`: Get author by ID
  - Response (200 OK):
    ```json
    {
      "id": 1,
      "name": "J.K. Rowling",
      "biography": "British author known for writing the Harry Potter series"
    }
    ```
  - Response (404 Not Found):
    ```json
    {
      "timestamp": "2023-04-27T19:12:41.295+00:00",
      "message": "Author not found with id: 999",
      "details": "uri=/api/authors/999"
    }
    ```

- `POST http://localhost:8080/api/authors`: Create a new author (Admin role required)
  - Request Headers:
    ```
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Request Body:
    ```json
    {
      "name": "J.K. Rowling",
      "biography": "British author known for writing the Harry Potter series"
    }
    ```
  - Response (201 Created):
    ```json
    {
      "id": 1,
      "name": "J.K. Rowling",
      "biography": "British author known for writing the Harry Potter series"
    }
    ```

- `PUT http://localhost:8080/api/authors/{id}`: Update an author (Admin role required)
  - Request Headers:
    ```
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Request Body:
    ```json
    {
      "name": "J.K. Rowling",
      "biography": "British author best known for writing the Harry Potter fantasy series"
    }
    ```
  - Response (200 OK):
    ```json
    {
      "id": 1,
      "name": "J.K. Rowling",
      "biography": "British author best known for writing the Harry Potter fantasy series"
    }
    ```

- `DELETE http://localhost:8080/api/authors/{id}`: Delete an author (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

- `DELETE http://localhost:8080/api/authors`: Delete all authors (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

### Books

- `GET http://localhost:8080/api/books`: Get all books
  - Response (200 OK):
    ```json
    [
      {
        "id": 1,
        "title": "Harry Potter and the Philosopher's Stone",
        "isbn": "9780747532743",
        "price": 19.99,
        "description": "The first novel in the Harry Potter series",
        "authorId": 1,
        "authorName": "J.K. Rowling"
      }
    ]
    ```

- `GET http://localhost:8080/api/books/{id}`: Get book by ID
  - Response (200 OK):
    ```json
    {
      "id": 1,
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "9780747532743",
      "price": 19.99,
      "description": "The first novel in the Harry Potter series",
      "authorId": 1,
      "authorName": "J.K. Rowling"
    }
    ```

- `GET http://localhost:8080/api/books/author/{authorId}`: Get books by author ID
  - Response (200 OK):
    ```json
    [
      {
        "id": 1,
        "title": "Harry Potter and the Philosopher's Stone",
        "isbn": "9780747532743",
        "price": 19.99,
        "description": "The first novel in the Harry Potter series",
        "authorId": 1,
        "authorName": "J.K. Rowling"
      },
      {
        "id": 2,
        "title": "Harry Potter and the Chamber of Secrets",
        "isbn": "9780747538486",
        "price": 21.99,
        "description": "The second novel in the Harry Potter series",
        "authorId": 1,
        "authorName": "J.K. Rowling"
      }
    ]
    ```

- `POST http://localhost:8080/api/books`: Create a new book (Admin role required)
  - Request Headers:
    ```
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Request Body:
    ```json
    {
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "9780747532743",
      "price": 19.99,
      "description": "The first novel in the Harry Potter series",
      "authorId": 1
    }
    ```
  - Response (201 Created):
    ```json
    {
      "id": 1,
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "9780747532743",
      "price": 19.99,
      "description": "The first novel in the Harry Potter series",
      "authorId": 1,
      "authorName": "J.K. Rowling"
    }
    ```

- `PUT http://localhost:8080/api/books/{id}`: Update a book (Admin role required)
  - Request Headers:
    ```
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Request Body:
    ```json
    {
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "9780747532743",
      "price": 24.99,
      "description": "Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive.",
      "authorId": 1
    }
    ```
  - Response (200 OK):
    ```json
    {
      "id": 1,
      "title": "Harry Potter and the Philosopher's Stone",
      "isbn": "9780747532743",
      "price": 24.99,
      "description": "Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive.",
      "authorId": 1,
      "authorName": "J.K. Rowling"
    }
    ```

- `DELETE http://localhost:8080/api/books/{id}`: Delete a book (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

- `DELETE http://localhost:8080/api/books`: Delete all books (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

### Users

- `GET http://localhost:8080/api/users`: Get all users (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (200 OK):
    ```json
    [
      {
        "id": 1,
        "username": "admin",
        "email": "admin@example.com",
        "role": "ADMIN"
      },
      {
        "id": 2,
        "username": "user",
        "email": "user@example.com",
        "role": "USER"
      }
    ]
    ```

- `GET http://localhost:8080/api/users/{id}`: Get user by ID (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (200 OK):
    ```json
    {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "role": "ADMIN"
    }
    ```

- `DELETE http://localhost:8080/api/users/{id}`: Delete a user (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

- `DELETE http://localhost:8080/api/users`: Delete all users (Admin role required)
  - Request Headers:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```
  - Response (204 No Content)

## Setup and Run

### Prerequisites
- Java 17 or higher
- Gradle (or use the Gradle wrapper included)

### Installation Steps
1. Clone the repository
   ```bash
   git clone https://github.com/misranrifat/book-store.git
   cd book-store
   ```

2. If you don't have the Gradle wrapper, initialize it
   ```bash
   gradle wrapper
   ```

3. Build the project
   ```bash
   ./gradlew build
   ```

4. Run the application
   ```bash
   ./gradlew bootRun
   ```

5. The application will start on http://localhost:8080

### Using Environment Variables
The application can be configured using environment variables. The repository includes a `set_db_env.sh` script to help set up the required environment variables:

1. Make the script executable:
   ```bash
   chmod +x set_db_env.sh
   ```

2. Source the script to set environment variables:
   ```bash
   source ./set_db_env.sh
   ```

3. The script sets the following environment variables:
   - Database connection details (host, port, database name, username, password)
   - Application port
   - JWT secret and expiration time

4. Once sourced, you can run the application using the environment variables:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=prod'
   ```

### Environment Profiles
The application supports two environment profiles:

1. **Development Profile (dev)**: Uses H2 in-memory database
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```

2. **Production Profile (prod)**: Uses PostgreSQL database
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=prod'
   ```

### H2 Database Console
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:bookstoredb
- Username: sa
- Password: (empty)

### Preloaded Data
The application comes with preloaded data for testing purposes:

1. **Users**:
   - Admin User:
     - Username: `admin`
     - Password: `admin123`
     - Role: ADMIN
   - Regular User:
     - Username: `user`
     - Password: `user123`
     - Role: USER

2. **Authors**:
   - J.K. Rowling (ID: 1)
   - George Orwell (ID: 2)

3. **Books**:
   - "Harry Potter and the Philosopher's Stone" by J.K. Rowling (ID: 1)
   - "Harry Potter and the Chamber of Secrets" by J.K. Rowling (ID: 2)
   - "1984" by George Orwell (ID: 3)

You can sign in with these credentials right away to test the API without needing to create new users.

### Setting Up an Admin User
1. Register a regular user
2. Connect to the H2 console
3. Run this SQL query to upgrade a user to ADMIN:
   ```sql
   UPDATE USERS SET ROLE = 'ADMIN' WHERE USERNAME = 'yourusername';
   ```

## Troubleshooting

### Common Issues

1. **403 Forbidden Error**:
   - This usually means you're trying to access an endpoint that requires ADMIN role.
   - Solution: Make sure your user has the ADMIN role and your token is valid.

2. **JWT Token Expiration**:
   - Tokens expire after 15 minutes.
   - Solution: Sign in again to get a new token.

3. **H2 Console Access Issues**:
   - Make sure you're using the correct JDBC URL.
   - Check that the console is enabled in application.yml.

4. **Missing Gradle Wrapper**:
   - If you see "No such file or directory: ./gradlew", you need to initialize the wrapper.
   - Solution: Run `gradle wrapper` first.

## Technologies

- Spring Boot 3.2.3
- Spring Security with JWT authentication
- Spring Data JPA
- H2 Database
- Gradle 8.x
- JSON Web Tokens (JWT) 

## Actuator Endpoints

Spring Boot Actuator provides production-ready features for monitoring and managing the application. The following endpoints are available:

- Health information: http://localhost:8080/actuator/health
- Application information: http://localhost:8080/actuator/info
- Application metrics: http://localhost:8080/actuator/metrics
- Environment details: http://localhost:8080/actuator/env
- Application beans: http://localhost:8080/actuator/beans
- Request mappings: http://localhost:8080/actuator/mappings

These endpoints are accessible without authentication for development purposes but should be properly secured in a production environment.

## Data Transfer Objects (DTOs)

The application uses the following DTOs for data transfer between the client and server:

### UserDto
- Fields: id, username, email, role
- Purpose: Used for user information transfer without exposing sensitive data
- Static factory method: `fromEntity(User)` to convert User entity to UserDto

### JwtResponse
- Fields: token, type ("Bearer"), id, username, email, role
- Purpose: Response sent after successful authentication containing JWT token and user info

### BookDto
- Fields: id, title, isbn, price, description, authorId, authorName
- Validation:
  - title: Not blank
  - price: Not null, positive value
  - authorId: Not null

### AuthorDto
- Fields: id, name, biography
- Validation:
  - name: Not blank

### LoginRequest
- Fields: username, password
- Purpose: Used for user authentication

### SignupRequest
- Fields: username, email, password
- Purpose: Used for user registration

### MessageResponse
- Field: message
- Purpose: Generic response for various operations

## Environment Variables

The application supports the following environment variables:

### Database Configuration
- `DB_HOST`: PostgreSQL host address
- `DB_PORT`: PostgreSQL port
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

### Application Configuration
- `APPLICATION_PORT`: Port the application runs on
- `JWT_SECRET`: Secret key for JWT token generation
- `JWT_EXPIRATION`: Token expiration time in milliseconds

### Validation Configuration
- `VALIDATION_ENABLED`: Enable/disable validation
- `VALIDATION_FAIL_FAST`: Whether to stop at first validation error

### API Configuration
- `API_VERSION`: API version
- `API_BASE_PATH`: Base path for API endpoints

These variables can be set using the provided `set_db_env.sh` script.

## Logging

The application uses SLF4J with Logback for logging. Logging is configured in `application.yml` with different settings for development and production environments:

### Log Levels
- **Development**: DEBUG level for application code, with more verbose Spring and Hibernate logging
- **Production**: INFO level for application code, with WARN level for Spring and Hibernate to reduce log volume

### Log Configuration
- **Log Format**: `%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n`
- **Log File**: Logs are written to `logs/bookstore.log`
- **Rolling Policy**: 
  - Maximum file size: 10MB
  - History: 7 days/files

### Controller Logging
All controllers use SLF4J logging to track:
- Incoming requests
- Operation results
- Error conditions
- Authentication and authorization events

Example log messages:
```
2023-05-01 12:00:00 [http-nio-8080-exec-1] INFO  c.b.controller.AuthController - Authentication attempt for user: admin
2023-05-01 12:00:00 [http-nio-8080-exec-1] INFO  c.b.controller.AuthController - User authenticated successfully: admin
2023-05-01 12:01:15 [http-nio-8080-exec-2] INFO  c.b.controller.BookController - Request to get all books
2023-05-01 12:01:15 [http-nio-8080-exec-2] DEBUG c.b.controller.BookController - Retrieved 5 books
```