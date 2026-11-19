# JavaWebMVC-JPA

Java 17, Servlet/JSP, JPA (Hibernate), SQL Server, Tomcat 11.

## Features
- Login, registration, OTP and password reset
- Category and video CRUD, upload and playback
- SiteMesh 3 with a Bootstrap 5 layout
- Form validation and user profiles
- Email-change OTP and Cloudinary avatar upload

## Run
1. Apply `Assignment03_Migration.sql` if needed, then `Assignment04_Migration.sql` to `JavaWebMVC`.
2. Configure SMTP and `CLOUDINARY_URL` privately in Tomcat `bin/setenv.bat` (see example).
3. Run `mvn clean package` and deploy `target/JavaWebMVC-JPA.war` to Tomcat 11.
4. Open `/login`, `/home`, `/video`, `/category/list` or `/profile`.
