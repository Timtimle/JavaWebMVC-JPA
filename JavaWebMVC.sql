
-- Tạo database
CREATE DATABASE JavaWebMVC;
GO

USE JavaWebMVC;
GO

-- Bảng tài khoản
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    fullname NVARCHAR(255),
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(255),
    roleid INT NOT NULL DEFAULT 2,
    phone VARCHAR(20),
    createdDate DATETIME2 DEFAULT GETDATE()
);
GO

-- Bảng danh mục
CREATE TABLE Category (
    cate_id INT IDENTITY(1,1) PRIMARY KEY,
    cate_name NVARCHAR(255) NOT NULL,
    icons NVARCHAR(255)
);
GO

-- Dữ liệu mẫu Category
INSERT INTO Category (cate_name, icons)
VALUES
(N'Điện thoại', NULL),
(N'Laptop', NULL),
(N'Phụ kiện', NULL);
GO

-- Kiểm tra kết quả
SELECT * FROM Users;
SELECT * FROM Category;
