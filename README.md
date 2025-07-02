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
- `GET /users`  
  Retrieve a list of all users.

- `GET /users/{userId}`  
  Retrieve a user by their ID.  
  **Response:**  
  - `200 OK` with the user object.  
  - `404 NOT FOUND` if the user does not exist.

- `DELETE /users/{userId}`  
  Delete a user by their ID.  
  **Response:**  
  - `200 OK` if deleted.  
  - `404 NOT FOUND` if the user does not exist.

- `PUT /users/{userId}`  
  Update a user's `fullName` and `phoneNumber`.  
  **Request Body:**
  ```json
  {
    "fullName": "string",
    "phoneNumber": "string"
  }
  ```
  **Response:**  
  - `200 OK` with the updated user object.  
  - `404 NOT FOUND` if the user does not exist.

- `PUT /users/{userId}/password`  
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

- `PUT /users/{userId}/image`  
  Update a user's profile image.  
  **Request Body:**
  ```json
  {
    "imageUrl": "string"
  }
  ```
  **Response:**  
  - `200 OK` with the updated user object.  
  - `404 NOT FOUND` if the user does not exist.

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

## References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/)
- See `HELP.md` for more guides and references

---

> This project provides a basic Spring Boot setup with a user entity, service, repository, and a REST endpoint for listing users.
