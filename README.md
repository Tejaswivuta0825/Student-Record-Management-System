# Student Record Management System

A Java-based Student Record Management System built using JDBC and MySQL.

## Features

- Add student records
- View all students
- Search students by ID
- Update student details
- Delete student records with confirmation
- Input validation and error handling
- MySQL database connectivity using JDBC

## Technologies Used

- Java
- MySQL 9.4
- JDBC
- MySQL Connector/J
- Visual Studio Code

## Database Setup

1. Create a MySQL database named `student_db`.
2. Create a `students` table with the columns `id`, `name`, `age`, `course`, and `email`.
3. Create a dedicated MySQL application user and grant it the required permissions.
4. Configure your database credentials locally in the `.env` file.

**Security:** Never upload `.env` or expose database passwords in source code.

## Project Structure

```text
StudentManagementSystem/
├── .gitignore
├── README.md
├── .env
├── lib/
│   └── mysql-connector-j-26.10.0.jar
├── src/
│   ├── DBConnection.java
│   ├── Main.java
│   ├── TestConnection.java
│   ├── ViewStudents.java
│   ├── SearchStudent.java
│   ├── UpdateStudent.java
│   └── DeleteStudent.java
└── out/
```

## Run the Application

Compile the application from the project root:

```bash
javac -cp "lib/*:out" -d out src/Main.java
```

Run it:

```bash
java -cp "out:lib/*" src.Main
```

Ensure MySQL is running and your local database credentials are configured before starting the application.

## Author

Student project developed for learning Java, JDBC, and MySQL database operations.