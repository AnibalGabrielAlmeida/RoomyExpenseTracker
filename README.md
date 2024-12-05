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
