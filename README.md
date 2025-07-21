Roomy Expense Tracker - Backend API

Roomy Expense Tracker is a backend application to manage and split expenses among people sharing the same property.  
It allows managing fixed and variable expenses, initially dividing them equally and later allowing customized splits.

---

## 🌐 Live API (Demo Deployment)

The backend is deployed and publicly accessible here:

- **Base URL:** https://roomyexpensetracker.onrender.com  
- **Swagger UI:** https://roomyexpensetracker.onrender.com/swagger-ui/index.html

> ⚠️ Note: This is a free Render instance. It may take **30–50 seconds** to wake up after a period of inactivity.

---

## Technologies

- Java 17 + Spring Boot  
- In-memory H2 database (development)  
- Swagger UI for documentation and testing  
- JPA / Hibernate for persistence  
- Postman collection for automated tests  

---

## 🖥️ Running Locally (optional)

The application uses an in-memory H2 database for local development. No environment variables are needed.

```bash
./mvnw spring-boot:run
```

Then access Swagger at:  
http://localhost:8080/swagger-ui/index.html

---

## 🛠️ Configuration

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

## 📲 Frontend Integration

To use the API from a frontend, use:

```js
fetch("https://roomyexpensetracker.onrender.com/api/user/getAll")
```

We recommend storing the API base URL in an environment variable or configuration file.

---

## 💡 Important Notes

- The current setup is for demo purposes only.
- Data is stored in-memory (H2), and resets when the server restarts or sleeps.
- There is no authentication or authorization yet – all endpoints are open.
- The backend is designed to be consumed by a separate frontend application, which is not included in this repository.

---

## Contact

Gabriel Almeida – almeida.g.dev@gmail.com

---

## Notes

This repository contains only the backend, separated from the frontend to facilitate independent deployments and collaboration with frontend teams.

---

## 📚 Main API Endpoints

### 👤 User Endpoints

| Method | Endpoint                                 | Description            |
|--------|------------------------------------------|------------------------|
| GET    | `/api/user/getAll`                       | Get all users          |
| GET    | `/api/user/getById/{id}`                 | Get user by ID         |
| POST   | `/api/user/createUser`                   | Create a new user      |
| PATCH  | `/api/user/updateUser/{userId}`          | Update user            |
| PATCH  | `/api/user/{userId}/role`                | Change user role       |
| DELETE | `/api/user/deleteById/{id}`              | Delete user by ID      |

### 🏠 House Endpoints

| Method | Endpoint                                         | Description                |
|--------|--------------------------------------------------|----------------------------|
| GET    | `/api/house/getAll`                              | Get all houses             |
| GET    | `/api/house/getById/{id}`                        | Get house by ID            |
| POST   | `/api/house/createHouse`                         | Create a new house         |
| PATCH  | `/api/house/updateHouse/{houseId}`               | Update house               |
| PATCH  | `/api/house/{houseId}/addRoommate/{userId}`      | Add user to house          |
| PATCH  | `/api/house/{houseId}/removeRoommate/{userId}`   | Remove user from house     |
| DELETE | `/api/house/deleteById/{id}`                     | Delete house by ID         |
| GET    | `/api/house/{houseId}/roommates`                 | Get roommates by house ID  |

### 💸 Expense Endpoints

| Method | Endpoint                                         | Description                |
|--------|--------------------------------------------------|----------------------------|
| GET    | `/api/expenses/getAll`                           | Get all expenses           |
| GET    | `/api/expenses/getById/{id}`                     | Get expense by ID          |
| POST   | `/api/expenses/createExpense`                    | Create a new expense       |
| PATCH  | `/api/expenses/updateExpense/{expenseId}`        | Update expense             |
| DELETE | `/api/expenses/deleteById/{id}`                  | Delete expense by ID       |

### 💳 Payment Endpoints

| Method | Endpoint                                         | Description                |
|--------|--------------------------------------------------|----------------------------|
| GET    | `/api/payments/getAll`                           | Get all payments           |
| GET    | `/api/payments/getById/{id}`                     | Get payment by ID          |
| POST   | `/api/payments/createPayment`                    | Create a new payment       |
| PATCH  | `/api/payments/updatePayment/{paymentId}`        | Update payment             |
| DELETE | `/api/payments/deleteById/{id}`                  | Delete payment by ID       |

### 🧾 Expense Split Endpoints

| Method | Endpoint                                             | Description                    |
|--------|------------------------------------------------------|--------------------------------|
| GET    | `/api/expense-splits/getAll`                         | Get all expense splits         |
| GET    | `/api/expense-splits/getById/{id}`                   | Get expense split by ID        |
| POST   | `/api/expense-splits/createExpenseSplit`             | Create a new expense split     |
| PATCH  | `/api/expense-splits/updateExpenseSplit/{expenseSplitId}` | Update expense split     |
| DELETE | `/api/expense-splits/deleteById/{id}`                | Delete expense split by ID     |

> For full details and testing, see the Swagger UI documentation linked above.

