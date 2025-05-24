# Hospital Management System

A JavaFX-based Hospital Management System that supports role-based access for Patients, Doctors, and Administrators. The system features login and registration, appointment management, patient vitals, prescription handling, and automated email notifications.

---

## 📂 SQL Schema Setup

**Path**: `src/main/java/schema.sql`

### Steps:

1. Open **MySQL Workbench**
2. **Create a new database** manually (you’ll link to it later)
3. Copy contents of `schema.sql` into MySQL Workbench
4. Execute the script to create required tables and schema

---

## 🛠️ Initial Configuration



### 🔌 1. Create and Connect to Your Database

Before running the project:

- Create a new database in MySQL
- Copy the **JDBC Connection String** by right-clicking your MySQL connection in Workbench
- Remember your:
    - **MySQL username** (default is often `root`)
    - **MySQL password**

### ⚙️ 2. Update Database Credentials

**File path**: `src/main/java/com/structure/DataBase/DBUtil.java`

Edit the following fields with your credentials:

```java
private static final String URL = "Your JDBC Connection String";
private static final String USER = "your username";
private static final String PASSWORD = "your password";
```
### 📧 3. Email Notification Setup

This system sends emails to users when needed (e.g., appointment confirmations, feedback).

#### Requirements:

- A **Gmail account** with **2-Step Verification** enabled.
- A **Gmail App Password** generated for your account.  
  (Generate it here: [Google App Passwords](https://myaccount.google.com/apppasswords))

#### Configure Email Credentials

**File path:** `src/main/java/com/structure/EmailSender/EmailSender.java`

Edit the following fields with your Gmail and generated app password:

```java
final String fromEmail = "your-email@gmail.com";     // Your Gmail address
final String password = "your-app-password";         // Your Gmail App Password
```


---

## ▶️ How to Run the Application

1. Make sure you have completed the **SQL Schema Setup** and **Initial Configuration** steps above.
2. Open your IDE (e.g., IntelliJ IDEA or Eclipse).
3. Import the Maven project (using the `pom.xml`).
4. Navigate to the main class:
5. Run the `SignIn.java` class to start the application.
 - **File path:** `src/main/java/com/structure/Logins/SignIn.java`

---

