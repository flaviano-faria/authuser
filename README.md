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
### User Endpoints
- `GET /users` - Retrieve a list of all users.
- `GET /users/{userId}` - Retrieve a user by their ID.
- `DELETE /users/{userId}` - Delete a user by their ID.

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

## References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/)
- See `HELP.md` for more guides and references

---

> This project provides a basic Spring Boot setup with a user entity, service, repository, and a REST endpoint for listing users.
