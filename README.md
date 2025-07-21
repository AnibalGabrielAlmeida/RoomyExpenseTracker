Roomy Expense Tracker - Backend API

Roomy Expense Tracker is a backend application to manage and split expenses among people sharing the same property.  
It allows managing fixed and variable expenses, initially dividing them equally and later allowing customized splits.

---

## Technologies

- Java 17 + Spring Boot  
- In-memory H2 database (development)  
- Swagger UI for documentation and testing  
- JPA / Hibernate for persistence  
- Postman collection for automated tests  

---

## Configuration

The application uses an in-memory H2 database for development, with no environment variables required.

```properties
spring.application.name=tracker
spring.datasource.url=jdbc:h2:mem:expensesdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=test
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## H2 Web Console

The H2 web console is available at:  
http://localhost:8080/h2-console

---

## API Documentation

Swagger documentation is available at:  
http://localhost:8080/swagger-ui/index.html

This interface allows testing all endpoints without running the backend locally.

A Postman collection is also included for comprehensive testing.

---

## Running Locally (optional)

If you want to run locally:

```bash
./mvnw spring-boot:run
```

Then access Swagger at the URL mentioned above.

---

## Deployment

Currently in development, with plans for public deployment for testing and remote access.

---

## Demo Deployment Notice

This deployment is intended for demonstration purposes only.  
It uses an in-memory H2 database, which means all data will be lost when the server restarts.  
Additionally, there is no authentication or authorization implemented, so the API endpoints are not secured.

This setup allows recruiters and collaborators to explore the API functionality and Swagger documentation quickly and easily, but it is **not suitable for production use**.

Please consider this as a prototype backend in active development, with plans to implement persistent storage and security features in future versions.

---

## Contact

Gabriel Almeida – almeida.g.dev@gmail.com

---

## Notes

This repository contains only the backend, separated from the frontend to facilitate independent deployments and collaboration with frontend teams.

