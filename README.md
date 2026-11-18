# JavaWebMVC-JPA

Java 17 web application using Servlet/JSP, JPA/Hibernate, SQL Server and Tomcat 11.

## Features
- Login, registration, email OTP, password reset and category management.
- Video CRUD with multipart upload (MP4, WebM, OGG; max 100 MB).
- `/home`: 10 latest videos.
- `/video`: current user's videos, 6 per page.
- Click a video title to watch it.

## Setup
1. Run `Assignment03_Migration.sql` in the `JavaWebMVC` database if not already applied.
2. Check the database connection in `src/main/resources/META-INF/persistence.xml`.
3. Keep SMTP settings in Tomcat's local `setenv.bat`. Set `JWM_VIDEO_DIR` to a writable folder.
4. Run `mvn clean package`, deploy `target/JavaWebMVC-JPA.war` to Tomcat 11, then restart Tomcat.
