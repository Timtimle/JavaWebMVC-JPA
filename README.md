# JavaWebMVC-JPA

Project Maven WAR: Servlet/JSP + SQL Server + Jakarta Persistence (JPA) / Hibernate.
Chuyển từ project JavaWebMVC (JDBC) sang JPA theo hai tài liệu của giảng viên.

## Chức năng được giữ nguyên

- Đăng ký tài khoản: BCrypt, kiểm tra username/email trùng.
- Đăng nhập bằng Session; Cookie/Remember Me (token SHA-256, 30 ngày); đăng xuất.
- CRUD Category: danh sách, tìm kiếm, thêm, sửa, xóa, upload ảnh.
- Phân quyền Admin/Manager cho CRUD Category.

## JPA thay đổi gì?

- `model/Category.java`, `model/User.java`, `model/RememberToken.java`: JPA Entity.
- `dao/impl/CategoryDAOImpl.java`: `persist`, `find`, `merge`, `remove`, JPQL.
- `dao/impl/UserDAOImpl.java`: JPQL và `persist`.
- `dao/impl/RememberTokenDAOImpl.java`: JPQL và `persist` (không còn JDBC trực tiếp).
- `config/JpaConfig.java`: quản lý `EntityManagerFactory` dùng chung.
- `src/main/resources/META-INF/persistence.xml`: cấu hình kết nối SQL Server.

Code mẫu thầy dùng bảng `categories(CategoryId, CategoryName, Images, status)`,
nhưng project gốc dùng `Category(cate_id, cate_name, icons)`. Entity trong project
này được ánh xạ đúng **schema gốc**, không thay cấu trúc SQL đã nộp.

## Chạy trên máy cũ

1. Dùng **JDK 17**, **Tomcat 11**, SQL Server Express, Maven.
2. Giữ database `JavaWebMVC` cũ; kiểm tra bảng `Users`, `Category`, `RememberTokens`.
   Nếu chưa tạo database, chạy lần lượt `JavaWebMVC.sql` và `RememberTokens.sql`.
   **Không chạy lại `JavaWebMVC.sql` khi database đã có dữ liệu.**
3. Kiểm tra `src/main/resources/META-INF/persistence.xml`.
   URL được giữ theo project cũ: `localhost:14333`, database `JavaWebMVC`,
   `integratedSecurity=true` (Windows Authentication). Nếu trước đây port hay
   cách đăng nhập SQL Server đã đổi, chỉnh ở **persistence.xml**.
   Nếu Windows Authentication báo thiếu DLL, kiểm tra `sqljdbc_auth`/`mssql-jdbc_auth`
   trong `E:\App\Tool\sqljdbc-auth` và Java library path của Tomcat;
   `.vscode/launch.json` đã giữ cấu hình debug từ project cũ và đổi sang test JPA.
4. `mvn clean package` để tạo `target/JavaWebMVC-JPA.war`.
5. Copy WAR vào `webapps` của Tomcat 11. Truy cập
   `http://localhost:8080/JavaWebMVC-JPA/login`.
6. Thử Register, Login, Remember Me, Logout, tìm kiếm/thêm/sửa/xóa Category.

**Lưu ý:** Giao diện Category vẫn dùng thư mục upload cố định trong 2 controller:
`E:\App\Tool\javawebmvc-uploads` (như project cũ). Tạo thư mục trên máy
hoặc đổi cả hai hằng `UPLOAD_DIR` nếu máy không có ổ này.

`hibernate.hbm2ddl.auto=none`: không để Hibernate tự sửa hoặc xóa bảng cũ.
**Chưa thể xác nhận chạy với SQL Server trên máy người dùng nếu không kiểm thử tại máy đó.**
