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
2. Configure your PostgreSQL database. Default settings (see `src/main/resources/application.yml`):
   - URL: `jdbc:postgresql://localhost:5432/ead-authuser`
   - Username: `postgres`
   - Password: `banco123`
   
   Update these values as needed for your environment.

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
- `src/main/resources/` - Configuration files
- `src/test/java/com/ead/authuser/` - Test classes

## References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/)
- See `HELP.md` for more guides and references

---

> This project currently provides a basic Spring Boot setup. No REST endpoints or business logic are implemented yet.
