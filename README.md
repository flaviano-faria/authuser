# Authuser Service

A comprehensive Spring Boot microservice for user authentication and management built with modern Java technologies. This service provides **user registration**, **authentication**, **user management**, and **instructor registration** capabilities with **Spring HATEOAS** support for hypermedia-driven REST APIs, comprehensive logging with **Log4j2**, **microservices communication** through modern RestClient with load balancing, **Eureka service discovery** for dynamic service registration, and **RabbitMQ messaging** for event-driven architecture.

## Requirements
- Java 21
- Maven
- PostgreSQL
- Eureka Server (for service discovery)
- RabbitMQ (CloudAMQP or local instance)

## Setup
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd authuser
   ```
2. Configure your PostgreSQL database (default database: `ead-authuser-v2`)
3. Start the Eureka Server (typically on port 8761)
4. Set up RabbitMQ (CloudAMQP or local instance)
5. Configure the course service URL, RabbitMQ, and Eureka settings in `application.yml`:
   ```yaml
   ead:
     api:
       url:
         course: 'http://ead-course-service/ead-course'
     broker:
       exchange:
         userEvent: ead.userevent
   
   spring:
     rabbitmq:
       addresses: amqps://username:password@beaver.rmq.cloudamqp.com/vhost
   
   eureka:
     client:
       service-url:
         defaultZone: 'http://localhost:8761/eureka'
     instance:
       hostname: localhost
   ```

## Running the Application
Use Maven to build and run the application:
```bash
./mvnw spring-boot:run
```
The application will start on [http://localhost:8087/ead-authuser/](http://localhost:8087/ead-authuser/).

## Start application with maven
Use the following command:
```bash
mvn spring-boot:run -DSpring-boot.run.arguments=--server.port=8085
```

## Key Features

### Modern HTTP Client
- **RestClient**: Spring's modern HTTP client (Spring Boot 3.2+)
- **Load Balancing**: @LoadBalanced integration with Eureka
- **Timeout Protection**: Configurable connection and read timeouts (5 seconds)
- **Service Discovery**: Automatic service instance resolution
- **No Deprecated APIs**: Future-proof implementation

### Microservices Architecture
- **Service-to-Service Communication**: RestClient with Eureka integration
- **Event-Driven Messaging**: RabbitMQ for asynchronous user events
- **Service Discovery**: Eureka client registration and discovery
- **Load Balancing**: Client-side load balancing across service instances

## Application Configuration

### Database Configuration
The application uses PostgreSQL as the primary database:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ead-authuser-v2
    username: postgres
    password: banco123
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

### Service Discovery Configuration
Eureka client configuration for service registration and discovery:

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

### External Service Configuration
Course service communication settings:

```yaml
ead:
  api:
    url:
      course: 'http://ead-course-service/ead-course'
  broker:
    exchange:
      userEvent: ead.userevent
```

### RabbitMQ Configuration
Message broker configuration for event-driven communication:

```yaml
spring:
  rabbitmq:
    addresses: amqps://username:password@beaver.rmq.cloudamqp.com/vhost
```

### Logging Configuration
Comprehensive logging setup with Log4j2:

```yaml
logging:
  level:
    com.ead: INFO
    org.hibernate: DEBUG
```

## Testing
Run tests with:
```bash
./mvnw test
```

## Project Structure
- `src/main/java/com/ead/authuser/` - Main application code
  - `controllers/` - REST controllers
    - `AuthenticationController` - User registration and authentication endpoints
    - `UserController` - User management endpoints with HATEOAS support
    - `UserCourseController` - User-course relationship endpoints (read-only)
    - `InstructorController` - Instructor subscription endpoints
  - `clients/` - REST clients for microservices communication
    - `CourseClient` - Client for course service communication
  - `publishers/` - Message publishers for event-driven communication
    - `UserEventPublisher` - Publishes user lifecycle events to RabbitMQ
  - `services/` - Service interfaces and implementations
    - `UserService` - User management service interface
    - `impl/` - Service implementations
    - `user/handler/` - User-specific handlers
  - `repositories/` - Spring Data JPA repositories
    - `UserRepository` - User data access layer
  - `models/` - Entity models
    - `UserModel` - User entity with HATEOAS support
  - `enums/` - Enum types
    - `UserStatus` - User status enumeration (ACTIVE, BLOCKED)
    - `UserType` - User type enumeration (ADMIN, USER, STUDENT, INSTRUCTOR)
    - `CourseStatus` - Course status enumeration (IN_PROGRESS, CONCLUDED)
    - `CourseLevel` - Course level enumeration (BEGINNER, INTERMEDIATE, ADVANCED)
  - `configs/` - Configuration classes
    - `RequestLoggingFilterConfig` - HTTP request logging configuration
    - `RestClientConfig` - REST client configuration with timeouts and Eureka load balancing
    - `DateConfig` - Date/time configuration
    - `ResolverConfig` - Specification argument resolver configuration
    - `RabbitmqConfig` - RabbitMQ configuration and message converter setup
  - `specifications/` - JPA Specification classes for dynamic queries
    - `SpecificationTemplate` - Template for user specifications
  - `validations/` - Custom validation classes and constraints
    - `UserValidator` - Custom user validation logic
    - `PasswordConstraint` - Password validation constraint
    - `PaswordConstraintImpl` - Password constraint implementation
  - `exceptions/` - Custom exception classes and global exception handling
    - `GlobalExceptionHandler` - Global exception handling
    - `NotFoundException` - Resource not found exception
    - `DuplicatedUsernameException` - Username conflict exception
    - `ErrorRecordResponse` - Error response DTO
    - `ErrorResponse` - Error response structure
  - `dtos/` - Data Transfer Objects
    - `UserRecordDto` - User data transfer object with validation groups
    - `CourseRecordDto` - Course data transfer object
    - `InstructorRecordDto` - Instructor subscription DTO
    - `ResponsePageDto` - Paginated response DTO
    - `UserEventDto` - User event message payload for RabbitMQ
- `src/main/resources/` - Configuration files
  - `application.yml` - Application configuration
- `src/test/java/com/ead/authuser/` - Test classes

## Logging Features

This application implements comprehensive logging using **Log4j2** for better performance and flexibility compared to the default Logback.

### Log4j2 Configuration

The application uses Log4j2 instead of the default Spring Boot logging:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

**Note:** The default `spring-boot-starter-logging` is excluded from `spring-boot-starter-web` to avoid conflicts.

### Logging Levels

Configured logging levels in `application.yml`:

```yaml
logging:
  level:
    com.ead: INFO
    org.hibernate: DEBUG
```

### Request Logging

The application includes a `RequestLoggingFilterConfig` that provides detailed HTTP request logging:

- **Query String**: Included in logs
- **Request Payload**: Included (up to 10,000 characters)
- **Headers**: Included (except Authorization header for security)
- **Response**: Logged for debugging purposes

This configuration helps with debugging and monitoring API requests.

### Application Logging

Controllers and services include strategic logging statements:

- **Debug Level**: Input parameters and method entry points
- **Warn Level**: Business logic warnings (e.g., password mismatches)
- **Error Level**: Exception handling and error conditions

**Example Log Output:**
```
DEBUG - POST registerUser received userRecordDTO: UserRecordDTO[username=john_doe, email=john@example.com, ...]
DEBUG - deleteUser received userId: 123e4567-e89b-12d3-a456-426614174000
WARN  - mismatched old password: 123e4567-e89b-12d3-a456-426614174000
ERROR - handleNotFoundException message: User not found with id: 123e4567-e89b-12d3-a456-426614174000
```


## Spring HATEOAS Features

This application implements **Spring HATEOAS** (Hypermedia as the Engine of Application State) to provide hypermedia-driven REST APIs. HATEOAS enables clients to navigate through the API dynamically by following links embedded in the responses.

### Key HATEOAS Features Implemented:

1. **RepresentationModel Integration**: The `UserModel` extends `RepresentationModel<UserModel>`, allowing it to include HATEOAS links.

2. **Dynamic Link Generation**: Using `WebMvcLinkBuilder` to create self-referencing links for resources.

3. **Hypermedia Responses**: API responses include `_links` section with navigable links.

### Dependencies
The application includes several key dependencies:

**Core Spring Boot Dependencies:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Spring HATEOAS:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

**Log4j2 Logging:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

**Eureka Client:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>4.3.0</version>
</dependency>
```

**AMQP Messaging:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**Database:**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.6</version>
    <scope>runtime</scope>
</dependency>
```

**Specification Support:**
```xml
<dependency>
    <groupId>net.kaczmarzyk</groupId>
    <artifactId>specification-arg-resolver</artifactId>
    <version>3.1.0</version>
</dependency>
```

**Note:** The default `spring-boot-starter-logging` is excluded from `spring-boot-starter-web` to use Log4j2 instead.

### HATEOAS Response Format
Responses from HATEOAS-enabled endpoints include a `_links` section with hypermedia links:

```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe",
  "userStatus": "ACTIVE",
  "userType": "USER",
  "phoneNumber": "+1234567890",
  "imageUrl": "https://example.com/image.jpg",
  "creationDate": "01-01-2024 10:00:00",
  "lastUpdateDate": "01-01-2024 10:00:00",
  "_links": {
    "self": {
      "href": "http://localhost:8087/ead-authuser/users/123e4567-e89b-12d3-a456-426614174000"
    }
  }
}
```

## REST API

### Authentication Endpoints
- `POST /auth/signup`  
  Register a new user.  
  **Request Body:**
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string",
    "fullName": "string",
    "phoneNumber": "string"
  }
  ```
  **Response:**  
  - `201 CREATED` with the created user object.

### User Endpoints

#### `GET /users`
Retrieve a paginated list of all users with HATEOAS links.

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 3) - Page size
- `sort` (default: userId,ASC) - Sort field and direction
- `courseId` (optional) - Filter users by course ID

**Response Example:**
```json
{
  "content": [
    {
      "userId": "123e4567-e89b-12d3-a456-426614174000",
      "username": "john_doe",
      "email": "john@example.com",
      "fullName": "John Doe",
      "userStatus": "ACTIVE",
      "userType": "USER",
      "phoneNumber": "+1234567890",
      "imageUrl": "https://example.com/image.jpg",
      "creationDate": "01-01-2024 10:00:00",
      "lastUpdateDate": "01-01-2024 10:00:00",
      "_links": {
        "self": {
          "href": "http://localhost:8087/ead-authuser/users/123e4567-e89b-12d3-a456-426614174000"
        }
      }
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 3,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "size": 3,
  "number": 0,
  "empty": false
}
```

#### `GET /users/{userId}`
Retrieve a user by their ID with HATEOAS links.

**Response:**  
- `200 OK` with the user object including `_links` section.
- `404 NOT FOUND` if the user does not exist.

**Response Example:**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe",
  "userStatus": "ACTIVE",
  "userType": "USER",
  "phoneNumber": "+1234567890",
  "imageUrl": "https://example.com/image.jpg",
  "creationDate": "01-01-2024 10:00:00",
  "lastUpdateDate": "01-01-2024 10:00:00",
  "_links": {
    "self": {
      "href": "http://localhost:8087/ead-authuser/users/123e4567-e89b-12d3-a456-426614174000"
    }
  }
}
```

#### `DELETE /users/{userId}`
Delete a user by their ID.  
**Response:**  
- `200 OK` if deleted.  
- `404 NOT FOUND` if the user does not exist.

#### `PUT /users/{userId}`
Update a user's `fullName` and `phoneNumber`.  
**Request Body:**
```json
{
  "fullName": "string",
  "phoneNumber": "string"
}
```
**Response:**  
- `200 OK` with the updated user object including HATEOAS links.  
- `404 NOT FOUND` if the user does not exist.

#### `PUT /users/{userId}/password`
Update a user's password.  
**Request Body:**
```json
{
  "oldPassword": "string",
  "password": "string"
}
```
**Response:**  
- `200 OK` if the password is updated successfully.  
- `409 CONFLICT` if the old password does not match.  
- `404 NOT FOUND` if the user does not exist.

#### `PUT /users/{userId}/image`
Update a user's profile image.  
**Request Body:**
```json
{
  "imageUrl": "string"
}
```
**Response:**  
- `200 OK` with the updated user object including HATEOAS links.  
- `404 NOT FOUND` if the user does not exist.

### User-Course Endpoints

#### `GET /users/{userId}/courses`
Retrieve all courses associated with a specific user.

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 10) - Page size
- `sort` (default: courseId,ASC) - Sort field and direction

**Response Example:**
```json
{
  "content": [
    {
      "courseId": "123e4567-e89b-12d3-a456-426614174000",
      "name": "Spring Boot Fundamentals",
      "description": "Learn Spring Boot from scratch",
      "imageUrl": "https://example.com/course-image.jpg",
      "courseStatus": "ACTIVE",
      "userInstructor": "456e7890-e89b-12d3-a456-426614174000",
      "courseLevel": "BEGINNER"
    }
  ],
  "page": {
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

**Response:**  
- `200 OK` with paginated course data.
- `500 INTERNAL SERVER ERROR` if course service communication fails.


### Instructor Endpoints

#### `POST /instructors/subscription`
Register a user as an instructor.

**Request Body:**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Response:**  
- `200 OK` with the updated user object (now with INSTRUCTOR user type).
- `404 NOT FOUND` if the user does not exist.

## Microservices Communication

### RestClient Configuration

The application uses **Spring's RestClient** (modern replacement for RestTemplate) with custom timeout settings and Eureka-based load balancing.

#### Configuration Class
```java
@Configuration
public class RestClientConfig {
    
    private static final int TIMEOUT = 5000;
    
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(customRequestFactory());
    }
    
    private ClientHttpRequestFactory customRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(TIMEOUT));
        factory.setReadTimeout(Duration.ofMillis(TIMEOUT));
        return factory;
    }
}
```

**Key Features:**
- **@LoadBalanced**: Enables client-side load balancing with Eureka service discovery
- **Custom Timeouts**: 5-second connection and read timeouts
- **SimpleClientHttpRequestFactory**: Reliable HTTP client implementation
- **No Deprecated APIs**: Uses modern Spring Boot 3.5+ approach

**Timeout Configuration:**
- **Connect Timeout**: 5000ms - Time to establish connection
- **Read Timeout**: 5000ms - Time to wait for response

**Benefits:**
- ✅ Automatic service discovery via Eureka
- ✅ Load balancing across multiple service instances
- ✅ Prevents hanging connections with timeouts
- ✅ Clean, maintainable code without deprecated APIs

### CourseClient

The application includes a `CourseClient` for communicating with the course microservice:

**Features:**
- **REST Client**: Uses Spring's `RestClient` for HTTP communication
- **Error Handling**: Comprehensive exception handling with logging
- **Pagination Support**: Handles paginated responses from course service
- **Configuration**: Base URL configurable via `application.yml`
- **Course Deletion**: Supports deleting user-course relationships in the course service
- **Eureka Integration**: Automatically discovers course service instances

**Configuration:**
```yaml
ead:
  api:
    url:
      course: 'http://ead-course-service/ead-course'
```

**Note:** The URL uses the Eureka service name (`ead-course-service`) instead of hardcoded host/port, enabling dynamic service discovery.

**Error Handling:**
- Logs errors with detailed messages
- Throws `RuntimeException` with cause for proper error propagation
- Graceful degradation when course service is unavailable
- Automatic retry via Eureka when service instances are available

## RabbitMQ Messaging Support

This application includes **RabbitMQ** support through Spring Boot's `spring-boot-starter-amqp`, enabling asynchronous messaging capabilities for microservices communication using the **Advanced Message Queuing Protocol (AMQP)**.

### RabbitMQ Features

- **Message Queuing**: Reliable message queuing and delivery with acknowledgments
- **Event-Driven Architecture**: Asynchronous event publishing for user lifecycle events
- **Fanout Exchange**: Broadcasting user events to multiple consumers
- **JSON Message Serialization**: Automatic JSON conversion for message payloads
- **Cloud Integration**: Support for cloud-hosted RabbitMQ instances (CloudAMQP)

### Architecture Overview

The application implements an **event-driven architecture** where user lifecycle events are published to RabbitMQ for consumption by other microservices:

```
Authuser Service → RabbitMQ → Other Microservices
     (Publisher)    (Broker)     (Consumers)
```

### RabbitMQ Configuration

#### CloudAMQP Integration
The application is configured to use **CloudAMQP** for managed RabbitMQ hosting:

```yaml
spring:
  rabbitmq:
    addresses: amqps://username:password@beaver.rmq.cloudamqp.com/vhost
```

#### Exchange Configuration
```yaml
ead:
  broker:
    exchange:
      userEvent: ead.userevent
```

### RabbitMQ Components

#### 1. RabbitmqConfig
The main configuration class that sets up RabbitMQ components:

```java
@Configuration
public class RabbitmqConfig {
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeUserEvent);
    }
}
```

**Key Features:**
- **RabbitTemplate**: Spring's high-level abstraction for RabbitMQ operations
- **Jackson2JsonMessageConverter**: Automatic JSON serialization/deserialization
- **JavaTimeModule**: Support for Java 8+ time types (LocalDateTime, etc.)
- **FanoutExchange**: Broadcasts messages to all bound queues

#### 2. UserEventPublisher
Publishes user lifecycle events to RabbitMQ:

```java
@Component
public class UserEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    public void publishUserEvent(UserEventDto userEventDto) {
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEventDto);
    }
}
```

**Usage Pattern:**
- Publishes user events when users are created, updated, or deleted
- Uses fanout exchange for broadcasting to multiple consumers
- Empty routing key (`""`) for fanout exchanges

#### 3. UserEventDto
The message payload structure for user events:

```java
public class UserEventDto {
    private UUID userId;
    private String userName;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String imageUrl;
    private String actionType; // CREATE, UPDATE, DELETE
}
```

**Event Types:**
- **CREATE**: User registration events
- **UPDATE**: User profile updates, status changes
- **DELETE**: User deletion events

### Message Flow

1. **User Operation**: User performs an action (register, update, delete)
2. **Event Creation**: Service creates a `UserEventDto` with event details
3. **Message Publishing**: `UserEventPublisher` sends the event to RabbitMQ
4. **Exchange Routing**: Fanout exchange broadcasts to all bound queues
5. **Consumer Processing**: Other microservices consume and process events

### Integration Points

The RabbitMQ integration is designed to notify other microservices about user lifecycle events:

- **Course Service**: Notify when users become instructors or are deleted
- **Notification Service**: Send welcome emails, profile update notifications
- **Analytics Service**: Track user registration and activity metrics
- **Audit Service**: Log user changes for compliance

### Security Considerations

- **SSL/TLS**: Uses `amqps://` for encrypted connections
- **Authentication**: Credentials embedded in connection URL
- **VHost Isolation**: Uses dedicated virtual host for service isolation

### Monitoring and Observability

- **Message Acknowledgment**: Automatic message acknowledgment for reliability
- **Connection Pooling**: CachingConnectionFactory for connection management
- **Error Handling**: Built-in retry mechanisms and dead letter queues (configurable)

### Best Practices Implemented

1. **Idempotent Operations**: Event consumers should handle duplicate messages
2. **Event Sourcing**: User events provide audit trail of all changes
3. **Loose Coupling**: Services communicate through events, not direct calls
4. **Scalability**: Fanout pattern allows multiple consumers per event type

### RabbitMQ Usage Examples

#### Publishing User Events
To publish user events in your service layer, inject the `UserEventPublisher`:

```java
@Service
public class UserServiceImpl implements UserService {
    
    private final UserEventPublisher userEventPublisher;
    
    @Override
    public UserModel registerUser(UserRecordDto userRecordDto) {
        UserModel userModel = // ... create user logic
        
        // Publish user creation event
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setUserId(userModel.getUserId());
        userEventDto.setUserName(userModel.getUsername());
        userEventDto.setEmail(userModel.getEmail());
        userEventDto.setFullName(userModel.getFullName());
        userEventDto.setUserStatus(userModel.getUserStatus().toString());
        userEventDto.setUserType(userModel.getUserType().toString());
        userEventDto.setActionType("CREATE");
        
        userEventPublisher.publishUserEvent(userEventDto);
        
        return userModel;
    }
}
```

#### Local RabbitMQ Setup
For local development, you can use Docker to run RabbitMQ:

```bash
# Run RabbitMQ with management UI
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management

# Access management UI at http://localhost:15672
# Default credentials: guest/guest
```

#### Local Configuration
For local RabbitMQ development, update `application.yml`:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

#### CloudAMQP Setup
1. Create a CloudAMQP account at [cloudamqp.com](https://cloudamqp.com)
2. Create a new instance (free tier available)
3. Copy the connection URL from the CloudAMQP dashboard
4. Update the `addresses` property in `application.yml`

#### Message Consumption Example
Other microservices can consume user events by implementing message listeners:

```java
@Component
public class UserEventConsumer {
    
    @RabbitListener(queues = "user.events.queue")
    public void handleUserEvent(UserEventDto userEventDto) {
        switch (userEventDto.getActionType()) {
            case "CREATE":
                // Handle user creation
                sendWelcomeEmail(userEventDto);
                break;
            case "UPDATE":
                // Handle user update
                updateUserCache(userEventDto);
                break;
            case "DELETE":
                // Handle user deletion
                cleanupUserData(userEventDto.getUserId());
                break;
        }
    }
}
```

### Troubleshooting RabbitMQ

#### Common Issues
1. **Connection Refused**: Check RabbitMQ server status and connection parameters
2. **Authentication Failed**: Verify username/password and virtual host permissions
3. **Message Not Delivered**: Check exchange and queue bindings
4. **SSL Certificate Issues**: Ensure proper certificate configuration for CloudAMQP

#### Monitoring
- **CloudAMQP Dashboard**: Monitor message rates, connections, and queues
- **Management UI**: Access RabbitMQ management interface for queue inspection
- **Application Logs**: Check Spring Boot logs for RabbitMQ connection status

#### Performance Tuning
- **Connection Pooling**: Adjust `CachingConnectionFactory` settings
- **Message Persistence**: Configure message durability based on requirements
- **Consumer Concurrency**: Set appropriate consumer thread counts

## Eureka Service Discovery

This application integrates with **Netflix Eureka** for service discovery, enabling dynamic service registration and discovery in a microservices architecture.

### Eureka Client Configuration

The application is configured as a Eureka client that registers itself with the Eureka server:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    hostname: localhost
```

### Service Registration

**Service Name:** `ead-authuser-service`

The application automatically registers itself with the Eureka server using the service name defined in `application.yml`:

```yaml
spring:
  application:
    name: ead-authuser-service
```

### Eureka Client Features

1. **Automatic Registration**: The service automatically registers itself with Eureka on startup
2. **Health Checks**: Eureka monitors the service health and availability
3. **Service Discovery**: Other services can discover this service through Eureka
4. **Load Balancing**: Eureka provides client-side load balancing capabilities
5. **Fault Tolerance**: Automatic service instance replacement when instances become unavailable

### Dependencies

The application includes the Eureka client dependency:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Service Discovery Benefits

1. **Dynamic Service Location**: Services can be discovered dynamically without hardcoded URLs
2. **Load Balancing**: Automatic load balancing across multiple service instances
3. **Fault Tolerance**: Automatic failover to healthy service instances
4. **Scalability**: Easy horizontal scaling of service instances
5. **Centralized Service Registry**: Single point of truth for all service locations

### Eureka Dashboard

Access the Eureka dashboard at `http://localhost:8761` to:
- View registered services
- Monitor service health
- See service instances and their status
- Manage service registrations

### Service Communication with Eureka

When communicating with other services, the application can use:
- **Service Discovery**: Look up services by name instead of hardcoded URLs
- **Load Balancing**: Automatically distribute requests across multiple instances
- **Circuit Breaker**: Implement fault tolerance patterns

### ResponsePageDto

The `ResponsePageDto` class handles paginated responses from external services:

**Features:**
- **Generic Type Support**: Works with any DTO type
- **JSON Deserialization**: Properly deserializes paginated responses
- **Page Metadata**: Preserves pagination information
- **Unknown Properties**: Ignores unknown JSON properties for flexibility

## Data Transfer Objects (DTOs)

### InstructorRecordDto

Represents instructor subscription data:

```java
public record InstructorRecordDto(
    @NotNull(message = "UserId is mandatory") UUID userId
)
```

**Fields:**
- `userId` - Unique user identifier to be registered as instructor

### UserCourseRecordDto

Represents user-course subscription data:

```java
public record UserCourseRecordDto(
    UUID userId,
    @NotNull(message = "CourseId is mandatory") UUID courseId
)
```

**Fields:**
- `userId` - User identifier (optional in request body)
- `courseId` - Course identifier to subscribe to

### CourseRecordDto

Represents course data received from the course microservice:

```java
public record CourseRecordDto(
    UUID courseId,
    String name,
    String description,
    String imageUrl,
    CourseStatus courseStatus,
    UUID userInstructor,
    CourseLevel courseLevel
)
```

**Fields:**
- `courseId` - Unique course identifier
- `name` - Course name
- `description` - Course description
- `imageUrl` - Course image URL
- `courseStatus` - Course status (ACTIVE, INACTIVE, etc.)
- `userInstructor` - Instructor user ID
- `courseLevel` - Course difficulty level (BEGINNER, INTERMEDIATE, ADVANCED)

### ResponsePageDto

Handles paginated responses from external services:

```java
public class ResponsePageDto<T> extends PageImpl<T> {
    private final PageMetadata page;
    // ... implementation details
}
```

**Features:**
- Extends Spring's `PageImpl` for compatibility
- Custom `PageMetadata` for external service pagination
- JSON deserialization support
- Generic type support for different DTOs

### Service Layer Methods

#### UserService
The `UserService` interface includes the following key methods:

**New Methods:**
- `registerInstructor(UserModel userModel)` - Registers a user as an instructor by updating their user type to INSTRUCTOR

**Existing Methods:**
- `findAll()` - Retrieve all users
- `findById(UUID userId)` - Find user by ID (throws NotFoundException if not found)
- `delete(UserModel userModel)` - Delete user and associated course subscriptions
- `registerUser(UserRecordDto userRecordDto)` - Register a new user
- `existsByUsername(String username)` - Check if username exists
- `existsByEmail(String email)` - Check if email exists
- `updateUser(UserRecordDto userRecordDto, UserModel userModel)` - Update user information
- `updatePassword(UserRecordDto userRecordDto, UserModel userModel)` - Update user password
- `updateImage(UserRecordDto userRecordDto, UserModel userModel)` - Update user profile image
- `findAll(Specification<UserModel> spec, Pageable pageable)` - Find users with specifications and pagination

#### UserCourseService
The `UserCourseService` interface manages user-course relationships:

**Methods:**
- `existsByUserAndCourseId(UserModel userModel, UUID courseId)` - Check if user-course subscription exists
- `save(UserCourseModel userCourseModel)` - Save user-course subscription
- `existsByCourseId(UUID courseId)` - Check if any user-course subscriptions exist for a course
- `deleteAllByCourseId(UUID courseId)` - Delete all user-course subscriptions for a specific course

### Repository Layer Methods

#### UserRepository
The `UserRepository` extends `JpaRepository` and `JpaSpecificationExecutor`:

**Methods:**
- `existsByUsername(String username)` - Check if username exists
- `existsByEmail(String email)` - Check if email exists
- Standard JPA methods: `findAll()`, `findById()`, `save()`, `delete()`, etc.
- Specification support for dynamic queries

#### UserCourseRepository
The `UserCourseRepository` manages user-course relationship data:

**Methods:**
- `existsByUserAndCourseId(UserModel userModel, UUID courseId)` - Check if specific user-course subscription exists
- `findAllUserCourseIntoUser(UUID userId)` - Find all course subscriptions for a specific user (native query)
- `existsByCourseId(UUID courseId)` - Check if any subscriptions exist for a course
- `deleteAllByCourseId(UUID courseId)` - Delete all subscriptions for a specific course
- Standard JPA methods: `save()`, `delete()`, etc.

### User Model
The `UserModel` entity includes the following fields:
- `userId` (UUID)
- `username` (String)
- `password` (String, not exposed in API)
- `email` (String)
- `fullName` (String)
- `userStatus` (Enum: `ACTIVE`, `BLOCKED`)
- `userType` (Enum: `ADMIN`, `USER`, `STUDENT`, `INSTRUCTOR`)
- `phoneNumber` (String)
- `imageUrl` (String)
- `creationDate` (LocalDateTime)
- `lastUpdateDate` (LocalDateTime)
- `_links` (HATEOAS links section)

### Exception Handling

The application uses a global exception handler to manage errors and provide consistent error responses. Custom exceptions can be defined and handled centrally.

- The application now throws `NotFoundException` in the service layer if a user is not found, which is handled globally to return a 404 error with a structured error response.

### Error Response Format
When an error is handled, the API returns a JSON response with the following structure:

```json
{
  "errorCode": 404,
  "errorMessage": "Resource not found",
  "errorDetails": {
    // Optional additional details
  }
}
```
- `errorCode`: HTTP status code (e.g., 404 for not found)
- `errorMessage`: Description of the error
- `errorDetails`: Optional map of additional error details

## UserRecordDTO

The `UserRecordDTO` is used as the request body for user-related endpoints. It includes validation constraints and different fields are required depending on the operation (registration, update, password change, image update).

| Field        | Type   | Required For           | Constraints & Notes                                                                 |
|--------------|--------|-----------------------|-------------------------------------------------------------------------------------|
| username     | String | Registration          | Required, 4-50 chars                                                               |
| email        | String | Registration          | Required, must be valid email format                                                |
| password     | String | Registration, Password| Required, 6-20 chars, must meet password policy                                     |
| oldPassword  | String | Password              | Required for password update, 6-20 chars, must meet password policy                 |
| fullName     | String | Registration, Update  | Required for registration, required for update                                      |
| phoneNumber  | String | Registration, Update  | Optional                                                                           |
| imageUrl     | String | Image Update          | Required for image update                                                           |

**Validation Groups:**
- **Registration:** Used in `POST /auth/signup` (username, email, password, fullName, phoneNumber)
- **Update:** Used in `PUT /users/{userId}` (fullName, phoneNumber)
- **Password:** Used in `PUT /users/{userId}/password` (oldPassword, password)
- **Image Update:** Used in `PUT /users/{userId}/image` (imageUrl)

**Example: Registration Request Body**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "fullName": "string",
  "phoneNumber": "string"
}
```

**Example: Update User Request Body**
```json
{
  "fullName": "string",
  "phoneNumber": "string"
}
```

**Example: Update Password Request Body**
```json
{
  "oldPassword": "string",
  "password": "string"
}
```

**Example: Update Image Request Body**
```json
{
  "imageUrl": "string"
}
```

## Benefits of HATEOAS Implementation

1. **Discoverability**: Clients can discover available actions by following links in responses
2. **Loose Coupling**: Clients don't need to know specific URL patterns
3. **Self-Documenting**: API responses include information about available operations
4. **Evolvability**: API can evolve without breaking clients that follow links
5. **REST Compliance**: Follows REST principles more closely with hypermedia controls

## API Navigation with HATEOAS

Clients can navigate the API by:
1. Starting with a known entry point
2. Following links in the `_links` section of responses
3. Using the `href` values to make subsequent requests
4. Discovering available operations dynamically

This approach makes the API more flexible and maintainable while providing a better developer experience.
