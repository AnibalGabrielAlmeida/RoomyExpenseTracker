# RoomyExpenseTracker

RoomyExpenseTracker is an application designed to manage shared house expenses and assign roles (such as admin or roommates). Users can be assigned the role of "admin" or "roomy," with the admin having special privileges to manage expenses and other settings for the house.

## **Technologies Used**
- **Java 17**
- **Spring Boot**
- **JPA (Hibernate)**
- **MySQL** (or any relational database)
- **Lombok** (to reduce boilerplate code)
- **Spring Web** (for REST controllers)
- **Spring Data JPA** (for persistence)

## **Project Status**
The project is currently under development with the goal of reaching its Minimum Viable Product (MVP). This README file will be updated periodically to reflect the functionalities as they are added.

## **MVP (Minimum Viable Product)**

### **1. Fixed and Variable Expense Division**
- Roommates can add and divide expenses such as rent, utilities, food, etc.
- The system allows expenses to be divided equally or based on **custom proportions** (e.g., one roommate pays more for using more resources).

### **2. User Management**
- Registration of roommates (name, email, phone number).
- Assignment of users to a house (relationship between users and houses).

### **3. Expense History**
- Each added expense is displayed in a history.
- Each expense has a date, category (rent, food, utilities), and the participants.

### **4. Debt Calculation**
- Automatic calculation of how much each roommate owes for each expense.
- A summary of what each one has paid and what they owe.

### **5. Basic Notifications**
- Simple reminders or notifications about what needs to be paid.

### **6. Basic User Interface (UI)**
- A simple interface where roommates can view expenses, add new ones, and check debts.

## **API Endpoints**

### **Expense Controller (/api/expenses)**

- POST /createExpense → Create a new expense.

- GET /getAll → Retrieve all expenses.

- GET /getById/{id} → Retrieve an expense by ID.

- DELETE /deleteById/{id} → Delete an expense by ID.

- PATCH /updateExpense/{expenseId} → Update an existing expense.

---

### **Expense Split Controller (/api/expense-splits)**

- POST /createExpenseSplit → Create a split for an expense.

- GET /getAll → Retrieve all expense splits.

- GET /getById/{id} → Retrieve an expense split by ID.

- DELETE /deleteById/{id} → Delete an expense split by ID.

- PATCH /updateExpenseSplit/{expenseSplitId} → Update an existing expense split.

### **House Controller (/api/house)**

- POST /createHouse → Create a new house.
- GET /getAll → Retrieve all houses.
- GET /getById/{id} → Retrieve a house by ID.
- DELETE /deleteById/{id} → Delete a house by ID.
- PATCH /updateHouse/{houseId} → Update an existing house.
- GET /{houseId}/roommates → Retrieve the list of roommates for a house.
- PATCH /{houseId}/addRoommate/{userId} → Add an existing user to a house.
- PATCH /{houseId}/removeRoommate/{userId} → Remove a user from a house.

---

### **Payment Controller (/api/payments)**

- POST /createPayment → Create a new payment.
- GET /getAll → Retrieve all payments.
- GET /getById/{id} → Retrieve a payment by ID.
- DELETE /deleteById/{id} → Delete a payment by ID.
- PATCH /updatePayment/{paymentId} → Update an existing payment.

---

### **User Controller (/api/user)**

- POST /createUser → Create a new user.
- GET /getAll → Retrieve all users.
- GET /getById/{id} → Retrieve a user by ID.
- DELETE /deleteById/{id} → Delete a user by ID.
- PATCH /updateUser/{userId} → Update an existing user.
- PATCH /{userId}/role → Change the role of a user.


# Installation & Setup

## Clone the repository:
```bash
git clone https://github.com/yourusername/RoomyExpenseTracker.git
cd RoomyExpenseTracker
```

## Build and run the application:
1. Run the following command to build the project:
    ```bash
    mvn clean install
    ```
2. Once the build is complete, start the application:
    ```bash
    mvn spring-boot:run
    ```

## API Access:
- The application will be available at [http://localhost:8080](http://localhost:8080).


## **API Testing with Postman**

To test the API endpoints of RoomyExpenseTracker, I recommend using [Postman](https://www.postman.com/). Below are the steps to follow and examples of how to test some of the endpoints.

### **Getting Started with Postman**
1. Download and install Postman from [here](https://www.postman.com/downloads/).
2. Import the Postman collection for this project (see the section below for more details).
3. Set the `BASE_URL` variable in Postman to:
   ```plaintext
   http://localhost:8080

### **Postman Collection**

You can download and import the Postman collection for RoomyExpenseTracker from the following link:

[Download Postman Collection](https://github.com/AnibalGabrielAlmeida/RoomyExpenseTracker/blob/main/RoomyExpense.postman_collection.json).

To import the collection into Postman:
1. Download the `RoomyExpense.postman_collection.json` file.
2. Open Postman.
3. Click on the "Import" button in the top-left corner.
4. Choose "File" and select the downloaded `RoomyExpense.postman_collection.json` file.
5. The collection will be imported into your Postman app, ready to use!

Alternatively, you can view the collection file directly in the repository.

## **Frontend**
The frontend is under development and will be implemented in a future version of the project. Currently, only the API endpoints are available for consumption by an external user interface.

## **Unit Testing and Integration Testing**
Unit and integration tests are in progress. These tests will cover core functionalities such as user creation, expense handling, and payments. More details will be provided as the testing phase progresses.

## **Project Structure**
(Empty for now, will be added later)

## **Environment Configuration**

The following configuration is for the Spring Boot application:

```properties
spring.application.name=tracker

# H2 Database Configuration for development
spring.datasource.url=jdbc:h2:mem:expensesdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=test
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
```

You can access the H2 console at http://localhost:8080/h2-console.
