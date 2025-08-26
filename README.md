# Authuser

A demo Spring Boot application for user authentication and management with **Spring HATEOAS** support for hypermedia-driven REST APIs, comprehensive logging with **Log4j2**, **API Composition** capabilities, and **microservices communication** through REST clients.

## Requirements
- Java 21
- Maven
- PostgreSQL

## Setup
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd authuser
   ```
2. Configure your PostgreSQL database
3. Configure the course service URL in `application.yml`:
   ```yaml
   ead:
     api:
       url:
         course: http://localhost:8088/ead-course
   ```

## Running the Application
Use Maven to build and run the application:
```bash
./mvnw spring-boot:run
```
The application will start on [http://localhost:8087/ead-authuser/](http://localhost:8087/ead-authuser/).

## Testing
Run tests with:
```bash
./mvnw test
```

## Project Structure
- `src/main/java/com/ead/authuser/` - Main application code
  - `controllers/` - REST controllers
    - `AuthenticationController` - User registration endpoints
    - `UserController` - User management endpoints with HATEOAS support
    - `UserCourseController` - User-course relationship endpoints
  - `clients/` - REST clients for microservices communication
    - `CourseClient` - Client for course service communication
  - `services/` - Service interfaces (e.g., `UserService`) and implementations (`impl/`)
  - `repositories/` - Spring Data JPA repositories (e.g., `UserRepository`, `UserCourseRepository`)
  - `models/` - Entity models (e.g., `UserModel`, `UserCourseModel`)
  - `enums/` - Enum types (e.g., `UserStatus`, `UserType`, `CourseStatus`, `CourseLevel`)
  - `configs/` - Configuration classes (e.g., `RequestLoggingFilterConfig`, `RestClientConfig`)
  - `dtos/` - Data Transfer Objects (e.g., `UserRecordDTO`, `CourseRecordDto`, `ResponsePageDto`)
- `src/main/resources/` - Configuration files
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

## API Composition

The application supports **API Composition** through the `UserCourseModel` entity, enabling users to be associated with multiple courses.

### UserCourseModel

The `UserCourseModel` represents the relationship between users and courses:

```java
@Entity
@Table(name = "TB_USERS_COURSES")
public class UserCourseModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String courseId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserModel user;
}
```

**Features:**
- **Lazy Loading**: User relationship is loaded only when needed
- **JSON Serialization**: Uses `@JsonInclude(JsonInclude.Include.NON_NULL)` for clean JSON output
- **Database Mapping**: Maps to `TB_USERS_COURSES` table
- **Many-to-One Relationship**: Multiple course enrollments per user

### API Composition Benefits

1. **Scalability**: Supports microservices architecture where course data comes from different services
2. **Performance**: Lazy loading prevents unnecessary data fetching
3. **Flexibility**: Easy to extend with additional course-related fields
4. **Data Integrity**: Proper foreign key relationships maintained

## Spring HATEOAS Features

This application implements **Spring HATEOAS** (Hypermedia as the Engine of Application State) to provide hypermedia-driven REST APIs. HATEOAS enables clients to navigate through the API dynamically by following links embedded in the responses.

### Key HATEOAS Features Implemented:

1. **RepresentationModel Integration**: The `UserModel` extends `RepresentationModel<UserModel>`, allowing it to include HATEOAS links.

2. **Dynamic Link Generation**: Using `WebMvcLinkBuilder` to create self-referencing links for resources.

3. **Hypermedia Responses**: API responses include `_links` section with navigable links.

### Dependencies
The application includes several key dependencies:

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

## Microservices Communication

### CourseClient

The application includes a `CourseClient` for communicating with the course microservice:

**Features:**
- **REST Client**: Uses Spring's `RestClient` for HTTP communication
- **Error Handling**: Comprehensive exception handling with logging
- **Pagination Support**: Handles paginated responses from course service
- **Configuration**: Base URL configurable via `application.yml`

**Configuration:**
```yaml
ead:
  api:
    url:
      course: http://localhost:8088/ead-course
```

**Error Handling:**
- Logs errors with detailed messages
- Throws `RuntimeException` with cause for proper error propagation
- Graceful degradation when course service is unavailable

### ResponsePageDto

The `ResponsePageDto` class handles paginated responses from external services:

**Features:**
- **Generic Type Support**: Works with any DTO type
- **JSON Deserialization**: Properly deserializes paginated responses
- **Page Metadata**: Preserves pagination information
- **Unknown Properties**: Ignores unknown JSON properties for flexibility

## Data Transfer Objects (DTOs)

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
