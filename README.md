# JavaWebMVC-JPA

Java Servlet/JSP application using MVC, three-tier architecture, JPA/Hibernate and SQL Server.

## Tech Stack
Java 17 · Jakarta EE · Hibernate ORM · Maven · SQL Server · Tomcat 11 · Jakarta Mail

## Features
- **Authentication:** BCrypt login, logout, session and Remember Me cookies.
- **Account security:** email OTP activation, resend protection and password reset.
- **Category:** CRUD, search and image uploads.
- **Video:** CRUD, multipart upload, latest 10 on `/home`, user videos with six per page on `/video`, streaming playback and view counts.
- **Persistence:** JPA entities, repositories and transactions; User–Video relationship (1:N).

## Project Structure
```text
JavaWebMVC-JPA/
├── src/main/java/vn/edu/hcmute/
│   ├── config/
│   ├── controller/
│   ├── dao/                     # JPA access
│   ├── model/                   # Entities
│   └── service/impl/            # Business logic, OTP, mail and storage
├── src/main/resources/META-INF/persistence.xml
├── src/main/webapp/
│   ├── WEB-INF/views/           # JSP pages
│   └── assets/css/
├── Assignment03_Migration.sql
├── JavaWebMVC.sql
├── RememberTokens.sql
└── pom.xml
```

## Setup
1. Keep the existing **JavaWebMVC** SQL Server database. Run `Assignment03_Migration.sql` in SSMS **once** (safe to rerun for existing tables).
2. Check the JDBC URL in `src/main/resources/META-INF/persistence.xml` (default: Windows Authentication, localhost port 14333).
3. Configure SMTP via Tomcat environment variables (see `setenv.bat.example`). **Never commit mail credentials.**
4. Optionally set `JWM_VIDEO_DIR` for video storage (default: `%USERPROFILE%\JavaWebMVC-JPA\videos`).
5. Run `mvn clean package`, copy `target/JavaWebMVC-JPA.war` to Tomcat 11 `webapps`, and restart Tomcat.

**Routes:** `/login`, `/register`, `/activate`, `/forgot-password`, `/reset-password`, `/home`, `/video`.

**Note:** SMTP credentials are required for real OTP delivery. SQL Server and SMTP must be tested in the local environment; `mvn clean package` alone does not verify integrations.
