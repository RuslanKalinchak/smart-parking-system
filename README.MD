## Local Setup Instructions

1. **Stop and remove any old containers:**
   ```bash
   docker-compose down -v
2. **Start PostgreSQL using Docker:**
   ```bash
   docker-compose up -d   
3. **Run the Spring Boot application:**
   ```bash
   ./gradlew bootRun
4. **Swagger link: http://localhost:8080/swagger-ui/index.html**

## Running Tests

**Execute all tests using:**
   ```bash
      ./gradlew test 