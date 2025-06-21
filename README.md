# Authuser

A demo Spring Boot application for user authentication and management.

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

## REST API

### Authentication Endpoints
- `POST /auth/signup` - Register a new user. 
    - **Request Body:**
      ```json
      {
        "username": "string",
        "email": "string",
        "password": "string",
        "fullName": "string",
        "phoneNumber": "string"
      }
      ```

### User Endpoints
- `GET /users` - Retrieve a list of all users.
- `GET /users/{userId}` - Retrieve a user by their ID.
- `DELETE /users/{userId}` - Delete a user by their ID.
- `PUT /users/{userId}` - Updates a user's `fullName` and `phoneNumber`.
    - **Request Body:**
      ```json
      {
        "fullName": "string",
        "phoneNumber": "string"
      }
      ```

#### User Model
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

## Exception Handling

The application uses a global exception handler to manage errors and provide consistent error responses. Custom exceptions can be defined and handled centrally.

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

### NotFoundException
A custom exception, `NotFoundException`, is defined for resource-not-found scenarios. It is handled globally to return a 404 error with a structured error response. However, as of now, the controllers do not throw this exception for missing users. Instead, if a user is not found, the application may return a 500 error due to an unhandled `NoSuchElementException`.

**Note:**
- For production use, it is recommended to update the controllers to throw `NotFoundException` when a user is not found, so the API returns a proper 404 error with a clear message.

## References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/)
- See `HELP.md` for more guides and references

---

> This project provides a basic Spring Boot setup with a user entity, service, repository, and a REST endpoint for listing users.
