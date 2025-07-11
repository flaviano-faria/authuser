# Authuser

A demo Spring Boot application for user authentication and management with **Spring HATEOAS** support for hypermedia-driven REST APIs.

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
  - `controllers/` - REST controllers (e.g., `UserController`)
  - `services/` - Service interfaces (e.g., `UserService`) and implementations (`impl/`)
  - `repositories/` - Spring Data JPA repositories (e.g., `UserRepository`)
  - `models/` - Entity models (e.g., `UserModel`)
  - `enums/` - Enum types (e.g., `UserStatus`, `UserType`)
- `src/main/resources/` - Configuration files
- `src/test/java/com/ead/authuser/` - Test classes

## Spring HATEOAS Features

This application implements **Spring HATEOAS** (Hypermedia as the Engine of Application State) to provide hypermedia-driven REST APIs. HATEOAS enables clients to navigate through the API dynamically by following links embedded in the responses.

### Key HATEOAS Features Implemented:

1. **RepresentationModel Integration**: The `UserModel` extends `RepresentationModel<UserModel>`, allowing it to include HATEOAS links.

2. **Dynamic Link Generation**: Using `WebMvcLinkBuilder` to create self-referencing links for resources.

3. **Hypermedia Responses**: API responses include `_links` section with navigable links.

### Dependencies
The application includes the Spring HATEOAS starter:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

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
