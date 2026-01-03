# Authuser Service

A production-ready Spring Boot microservice for user authentication and management, built with modern Java technologies and microservices best practices. This service provides comprehensive user management capabilities including registration, JWT-based authentication, role-based access control (RBAC), instructor management, and seamless integration with other microservices.

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication with configurable expiration
- **User Management**: Complete CRUD operations with validation and HATEOAS support
- **Role-Based Access Control**: Fine-grained authorization with Spring Security
- **Microservices Communication**: RestClient with Eureka service discovery and load balancing
- **Event-Driven Architecture**: RabbitMQ integration for asynchronous event publishing
- **Fault Tolerance**: Resilience4j Circuit Breaker for resilient service communication
- **Configuration Management**: Spring Cloud Config Server integration
- **Production Monitoring**: Spring Boot Actuator for health checks and metrics
- **Dynamic Queries**: JPA Specifications for flexible data filtering
- **Comprehensive Logging**: Log4j2 for production-grade logging

## 📋 Table of Contents

- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Architecture Overview](#architecture-overview)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Security](#security)
- [Microservices Integration](#microservices-integration)
- [Development Guide](#development-guide)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

## 📦 Requirements

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Eureka Server** (for service discovery)
- **RabbitMQ** (CloudAMQP or local instance)
- **Spring Cloud Config Server** (optional, for centralized configuration)

## 🏃 Quick Start

### 1. Clone the Repository

```bash
git clone <repo-url>
cd authuser
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE ead-authuser-v2;
```

### 3. Configuration

Configure your `application.yml` or use Spring Cloud Config Server:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ead-authuser-v2
    username: postgres
    password: your_password
  application:
    name: ead-authuser-service

ead:
  auth:
    jwtSecret: your-secret-key-min-64-characters-long-for-hmac-sha256
    jwtExpirationMs: 86400000  # 24 hours
  api:
    url:
      course: 'http://ead-course-service/ead-course'
  broker:
    exchange:
      userEvent: ead.userevent

eureka:
  client:
    service-url:
      defaultZone: 'http://localhost:8761/eureka'
  instance:
    hostname: localhost

spring:
  rabbitmq:
    addresses: amqps://username:password@beaver.rmq.cloudamqp.com/vhost
```

### 4. Run the Application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or with a custom port:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085
```

The application will start on `http://localhost:8087/ead-authuser/` (default configuration).

## 🏗️ Architecture Overview

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Authuser Service                          │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Controllers  │  │   Services   │  │ Repositories │      │
│  │  (REST API)  │→ │  (Business)  │→ │   (Data)     │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                 │                    │            │
│         │                 │                    │            │
│         ▼                 ▼                    ▼            │
│  ┌────────────────────────────────────────────────────┐     │
│  │              PostgreSQL Database                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  RestClient  │  │  RabbitMQ     │  │   Eureka      │      │
│  │  (Course)    │  │  (Events)     │  │ (Discovery)   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                 │                    │            │
└─────────┼─────────────────┼────────────────────┼──────────┘
          │                 │                    │
          ▼                 ▼                    ▼
    Course Service    Other Services      Config Server
```

### Key Architectural Patterns

1. **Layered Architecture**: Clear separation between controllers, services, and repositories
2. **Event-Driven**: Asynchronous messaging via RabbitMQ for loose coupling
3. **Circuit Breaker**: Fault tolerance for external service calls
4. **Service Discovery**: Dynamic service location via Eureka
5. **Configuration Management**: Centralized config via Spring Cloud Config
6. **Transaction Management**: ACID compliance for data consistency

## 📚 API Documentation

### Authentication Endpoints

#### `POST /auth/signup`

Register a new user.

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "fullName": "John Doe",
  "phoneNumber": "+1234567890"
}
```

**Validation Rules:**
- `username`: Required, 4-50 characters, unique
- `email`: Required, valid email format, unique
- `password`: Required, 6-20 characters, must meet password policy
- `fullName`: Required
- `phoneNumber`: Optional

**Response:** `201 CREATED` with user object (includes HATEOAS links)

#### `POST /auth/login`

Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "SecurePass123!"
}
```

**Response:** `201 CREATED`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Note:** Include the token in subsequent requests using the `Authorization: Bearer <token>` header.

### User Management Endpoints

#### `GET /users`

Retrieve a paginated list of users with optional filtering.

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 3) - Page size
- `sort` (default: userId,ASC) - Sort field and direction
- `userType` (optional) - Filter by user type (ADMIN, USER, STUDENT, INSTRUCTOR)
- `userStatus` (optional) - Filter by status (ACTIVE, BLOCKED)
- `email` (optional) - Search by email (LIKE pattern)
- `username` (optional) - Search by username (LIKE pattern)
- `fullName` (optional) - Search by full name (case-insensitive)
- `courseId` (optional) - Filter users by course ID

**Example:**
```
GET /users?userType=INSTRUCTOR&userStatus=ACTIVE&page=0&size=10&sort=userId,ASC
```

**Response:** `200 OK` with paginated user list including HATEOAS links

#### `GET /users/{userId}`

Retrieve a specific user by ID.

**Response:** `200 OK` with user object including `_links` section

#### `PUT /users/{userId}`

Update user information (fullName, phoneNumber).

**Request Body:**
```json
{
  "fullName": "John Updated",
  "phoneNumber": "+9876543210"
}
```

**Response:** `200 OK` with updated user object

#### `PUT /users/{userId}/password`

Update user password.

**Request Body:**
```json
{
  "oldPassword": "OldPass123!",
  "password": "NewPass123!"
}
```

**Response:** 
- `200 OK` if password updated successfully
- `409 CONFLICT` if old password doesn't match

#### `PUT /users/{userId}/image`

Update user profile image.

**Request Body:**
```json
{
  "imageUrl": "https://example.com/profile.jpg"
}
```

**Response:** `200 OK` with updated user object

#### `DELETE /users/{userId}`

Delete a user and associated course subscriptions.

**Response:** `200 OK` if deleted successfully

### User-Course Endpoints

#### `GET /users/{userId}/courses`

Retrieve all courses associated with a specific user.

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 10) - Page size
- `sort` (default: courseId,ASC) - Sort field and direction

**Response:** `200 OK` with paginated course data

### Instructor Endpoints

#### `POST /instructors/subscription`

Register a user as an instructor.

**Request Body:**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Response:** `200 OK` with updated user object (now with INSTRUCTOR user type)

## ⚙️ Configuration

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ead-authuser-v2
    username: postgres
    password: your_password
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        jdbc:
          lob:
            non-contextual-creation: true
```

### JWT Configuration

```yaml
ead:
  auth:
    jwtSecret: your-secret-key-min-64-characters-long-for-hmac-sha256
    jwtExpirationMs: 86400000  # 24 hours in milliseconds
```

**Security Note:** The JWT secret must be at least 64 characters long for HMAC-SHA256. Use a strong, randomly generated secret in production.

### Service Discovery (Eureka)

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    hostname: localhost

spring:
  application:
    name: ead-authuser-service
```

### RabbitMQ Configuration

```yaml
spring:
  rabbitmq:
    addresses: amqps://username:password@beaver.rmq.cloudamqp.com/vhost

ead:
  broker:
    exchange:
      userEvent: ead.userevent
```

### Resilience4j Circuit Breaker

```yaml
resilience4j:
  circuitbreaker:
    instances:
      circuitBreakerInstance:
        sliding-window-size: 30
        permitted-number-of-calls-in-half-open-state: 2
        sliding-window-type: TIME_BASED
        minimum-number-of-calls: 2
        wait-duration-in-open-state: 15s
        failure-rate-threshold: 80

  retry:
    instances:
      retryInstance:
        max-attempts: 3
        wait-duration: 5s
```

### Spring Cloud Config Server

```yaml
spring:
  config:
    import: 'optional:configserver:'
  cloud:
    config:
      discovery:
        service-id: ead-config-server
      username: configserver
      password: ead123
```

### Logging Configuration

```yaml
logging:
  level:
    com.ead: INFO
    org.hibernate: DEBUG
    org.springframework.security: DEBUG
```

### Actuator Configuration

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,refresh,metrics
  endpoint:
    health:
      show-details: when-authorized
```

## 🔒 Security

### JWT Authentication

The service implements JWT-based authentication using Spring Security:

1. **Token Generation**: Tokens are generated upon successful login
2. **Token Validation**: All protected endpoints validate JWT tokens
3. **Token Expiration**: Configurable expiration time (default: 24 hours)
4. **Security Filter**: `AuthenticationJwtFilter` intercepts requests and validates tokens

### Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    // Security filter chain configuration
    // JWT authentication filter
    // Password encoder (BCrypt)
    // Authentication manager
}
```

### Protected Endpoints

- **Public Endpoints**: `/auth/**` (signup, login)
- **Protected Endpoints**: All other endpoints require authentication
- **Role-Based Access**: `GET /users/**` requires `USER` role

### Password Security

- Passwords are hashed using BCrypt (via `PasswordEncoderFactories.createDelegatingPasswordEncoder()`)
- Passwords are never logged or exposed in API responses
- Password validation enforces complexity requirements

## 🔗 Microservices Integration

### RestClient Configuration

The service uses Spring's modern `RestClient` for HTTP communication:

**Features:**
- **Load Balancing**: `@LoadBalanced` integration with Eureka
- **Timeout Protection**: 5-second connection and read timeouts
- **Service Discovery**: Automatic service instance resolution
- **No Deprecated APIs**: Future-proof implementation

**Configuration:**
```java
@Configuration
public class RestClientConfig {
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(customRequestFactory());
    }
}
```

### Course Service Communication

The `CourseClient` provides integration with the course microservice:

- **Circuit Breaker**: Resilience4j integration for fault tolerance
- **Fallback Methods**: Graceful degradation when service is unavailable
- **Pagination Support**: Handles paginated responses
- **Error Handling**: Comprehensive exception handling with logging

### RabbitMQ Event Publishing

User lifecycle events are published to RabbitMQ:

**Event Types:**
- `CREATE`: User registration events
- `UPDATE`: User profile updates, status changes
- `DELETE`: User deletion events

**Event Flow:**
1. User operation occurs (create/update/delete)
2. Service creates `UserEventDto` with event details
3. `UserEventPublisher` sends event to RabbitMQ fanout exchange
4. Exchange broadcasts to all bound queues
5. Other microservices consume and process events

## 🛠️ Development Guide

### Project Structure

```
src/main/java/com/ead/authuser/
├── controllers/          # REST API endpoints
│   ├── AuthenticationController.java
│   ├── UserController.java
│   ├── UserCourseController.java
│   └── InstructorController.java
├── services/             # Business logic layer
│   ├── UserService.java
│   └── impl/
├── repositories/         # Data access layer
│   ├── UserRepository.java
│   └── RoleRepository.java
├── models/               # Entity models
│   ├── UserModel.java
│   └── RoleModel.java
├── dtos/                 # Data Transfer Objects
│   ├── UserRecordDto.java
│   ├── LoginRecordDto.java
│   └── JwtRecordDto.java
├── configs/              # Configuration classes
│   ├── WebSecurityConfig.java
│   ├── RestClientConfig.java
│   ├── RabbitmqConfig.java
│   └── security/         # Security components
│       ├── JwtProvider.java
│       ├── AuthenticationJwtFilter.java
│       └── UserDetailsServiceImpl.java
├── clients/              # External service clients
│   └── CourseClient.java
├── publishers/           # Event publishers
│   └── UserEventPublisher.java
├── validations/          # Custom validators
│   └── PasswordConstraint.java
├── specifications/       # JPA Specifications
│   └── SpecificationTemplate.java
└── exceptions/           # Exception handling
    └── GlobalExceptionHandler.java
```

### Key Design Patterns

#### 1. Validation Groups with JsonView

Different validation rules per endpoint using validation groups:

```java
@PostMapping("/signup")
public ResponseEntity<Object> registerUser(
    @RequestBody 
    @Validated(UserRecordDto.UserView.RegistrationPost.class)
    @JsonView(UserRecordDto.UserView.RegistrationPost.class)
    UserRecordDto userRecordDto) {
    // Only RegistrationPost fields are validated and deserialized
}
```

#### 2. Dynamic Query Specifications

Type-safe dynamic queries using JPA Specifications:

```java
@GetMapping
public ResponseEntity<Page<UserModel>> getAllUsers(
    SpecificationTemplate.UserSpec spec,
    Pageable pageable) {
    return ResponseEntity.ok(userService.findAll(spec, pageable));
}
```

**Query Examples:**
- `GET /users?userType=INSTRUCTOR&userStatus=ACTIVE`
- `GET /users?email=@example.com&fullName=john`

#### 3. Transaction Management

All write operations use `@Transactional` for ACID compliance:

```java
@Transactional
public UserModel registerUser(UserRecordDto userRecordDto) {
    UserModel userModel = // ... create user
    userRepository.save(userModel);
    userEventPublisher.publishUserEvent(userModel.toUserEventDto(ActionType.CREATE));
    return userModel;
}
```

#### 4. HATEOAS Support

Resources include hypermedia links for API discoverability:

```json
{
  "userId": "...",
  "username": "john_doe",
  "_links": {
    "self": {
      "href": "http://localhost:8087/ead-authuser/users/..."
    }
  }
}
```

### Best Practices

1. **Dependency Injection**: Constructor-based injection for immutability
2. **Exception Handling**: Centralized via `GlobalExceptionHandler`
3. **Logging**: Structured logging with appropriate levels
4. **Validation**: Input validation at controller level
5. **Security**: Never log sensitive data (passwords, tokens)
6. **Transactions**: Keep transactions short and focused
7. **Event Publishing**: Publish events after successful database operations

## 🧪 Testing

Run tests with:

```bash
./mvnw test
```

### Test Structure

```
src/test/java/com/ead/authuser/
└── AuthuserApplicationTests.java
```

## 🐛 Troubleshooting

### Common Issues

#### Service Not Registering with Eureka

**Symptoms:** Service doesn't appear in Eureka dashboard

**Solutions:**
- Verify Eureka server is running on port 8761
- Check `eureka.client.service-url.defaultZone` configuration
- Ensure network connectivity to Eureka server
- Review application logs for registration errors

#### JWT Authentication Failing

**Symptoms:** 401 Unauthorized errors on protected endpoints

**Solutions:**
- Verify JWT secret is configured and at least 64 characters
- Check token expiration time
- Ensure `Authorization: Bearer <token>` header is included
- Verify token hasn't expired
- Check security filter chain configuration

#### Circuit Breaker Always Open

**Symptoms:** All external service calls fail with fallback

**Solutions:**
- Check failure threshold configuration
- Verify minimum number of calls requirement
- Review wait duration in open state
- Check underlying service health
- Review logs for actual failure causes

#### RabbitMQ Connection Issues

**Symptoms:** Events not being published, connection refused

**Solutions:**
- Verify RabbitMQ server is running
- Check connection URL format (amqps:// for CloudAMQP)
- Verify credentials and virtual host
- Check network connectivity
- Review SSL certificate configuration (for CloudAMQP)

#### Database Connection Issues

**Symptoms:** Application fails to start, connection timeout

**Solutions:**
- Verify PostgreSQL is running
- Check database credentials
- Verify database exists
- Check connection pool configuration
- Review network connectivity

### Debugging Tips

1. **Enable Debug Logging:**
   ```yaml
   logging:
     level:
       com.ead: DEBUG
       org.springframework.web: DEBUG
       org.hibernate.SQL: DEBUG
   ```

2. **Check Actuator Endpoints:**
   - `/actuator/health` - Service health
   - `/actuator/info` - Application information
   - `/actuator/metrics` - Performance metrics

3. **Review Application Logs:**
   - Check startup logs for configuration issues
   - Review exception stack traces
   - Monitor request/response logs

## 📦 Dependencies

### Core Dependencies

- **Spring Boot 3.5.0**: Application framework
- **Spring Cloud 2025.0.0**: Microservices support
- **Java 21**: Programming language
- **PostgreSQL 42.7.6**: Database driver

### Key Libraries

- **Spring Security**: Authentication and authorization
- **JWT (jjwt 0.13.0)**: Token generation and validation
- **Spring HATEOAS**: Hypermedia-driven REST APIs
- **Resilience4j**: Circuit breaker and fault tolerance
- **Log4j2**: Advanced logging
- **Specification Arg Resolver 3.1.0**: Dynamic query building

See `pom.xml` for complete dependency list.


## 🤝 Contributing

1. Follow the existing code structure and patterns
2. Ensure all tests pass
3. Update documentation for new features
4. Follow security best practices
5. Use meaningful commit messages


**Built with using Spring Boot and modern microservices patterns**
