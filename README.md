# JavaWebMVC-JPA

A Java web application implementing MVC and three-tier architecture with JPA/Hibernate for database persistence.

## Tech Stack
Java 17 · Jakarta EE · Hibernate ORM · Maven · SQL Server · Tomcat 11

## Features
- **Authentication:** Login, registration, logout, BCrypt password hashing.
- **Session Management:** Cookie-based authentication, sessions, and Remember Me.
- **User Validation:** Duplicate username/email checks.
- **Authorization:** Role-based access control (Admin/Manager).
- **Category Management:** Create, read, update, delete, and search categories.
- **Image Handling:** Category image uploads.
- **Persistence:** JPA entities, Hibernate ORM, and transaction management.

## Project Structure

```text
JavaWebMVC-JPA/
├── src/
│   └── main/
│       ├── java/vn/edu/hcmute/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dao/
│       │   │   └── impl/
│       │   ├── model/
│       │   └── service/
│       │       └── impl/
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── views/
│           │   └── web.xml
│           └── assets/
├── JavaWebMVC.sql
├── RememberTokens.sql
└── pom.xml
```

## Build & Deployment

```bash
mvn clean package
```

Deploy `target/JavaWebMVC-JPA.war` to Apache Tomcat 11.