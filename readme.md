### School Management System

A project for managing a school system. The system includes functionality for handling courses, users (students and administrators), and registration.

### Technologies

- Java 17
- Spring Boot
- PostgreSQL
- Docker

### Running the Project

To run the project, follow these steps:

1. **Configure Docker Compose**

   Ensure that you have Docker installed on your system. Then, use the following command:

   ```bash
   docker-compose up
   ```

   This command will start the Spring Boot application and PostgreSQL database in Docker containers.

2. **Accessing the Application**

   After running, the application will be accessible at [http://localhost:8090/api](http://localhost:8090/api).

3. **Swagger API Documentation**

   The API documentation is available through Swagger UI at [http://localhost:8090/api/swagger-ui.html](http://localhost:8090/api/swagger-ui.html).

### Project Structure

The project structure includes the following components:

- **src**: Source code for the Spring Boot application.
- **Dockerfile**: Docker configuration for building the application image.
- **docker-compose.yml**: Docker Compose configuration for running the application and PostgreSQL in containers.
- **src/main/resources/application-docker.properties**: Configuration properties for Docker environment.
- **src/main/resources/application.properties**: Configuration properties for local environment.

### Building and Running Locally

If you want to build and run the project locally without Docker, make sure you have Java 17 installed.

The application will be accessible at [http://localhost:8080/api](http://localhost:8080/api).

### Notes

Make sure to adjust configurations, such as database URLs or application properties, according to your specific requirements in the appropriate property files.