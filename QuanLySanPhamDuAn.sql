
CREATE DATABASE QuanLySanPham

USE QuanLySanPham

-- Tạo bảng Product
CREATE TABLE Product (
    id Int PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(18, 2) NOT NULL,
    product_code NVARCHAR(100) UNIQUE NOT NULL,
    quantity Int NOT NULL,
    status NVARCHAR(50) NOT NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    modifiedDate DATETIME,
    createdBy NVARCHAR(255),
    modifiedBy NVARCHAR(255)
);

-- Tạo bảng Category
CREATE TABLE Category (
    id Int PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    category_code NVARCHAR(100) UNIQUE NOT NULL,
    status NVARCHAR(50) NOT NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    modifiedDate DATETIME,
    createdBy NVARCHAR(255),
    modifiedBy NVARCHAR(255)
);

-- Tạo bảng trung gian ProductCategory cho quan hệ nhiều-nhiều
CREATE TABLE ProductCategory (
    productId Int,
    categoryId Int,
    createdDate DATETIME DEFAULT GETDATE(),
    modifiedDate DATETIME,
    createdBy NVARCHAR(255),
    modifiedBy NVARCHAR(255),
    PRIMARY KEY (productId, categoryId),
    FOREIGN KEY (productId) REFERENCES Product(id) ON DELETE CASCADE,
    FOREIGN KEY (categoryId) REFERENCES Category(id) ON DELETE CASCADE
);


-- Thêm dữ liệu mẫu cho bảng Category
INSERT INTO Category (name, description, category_code, status, createdBy)
VALUES 
(N'Đồ uống', N'Các loại đồ uống giải khát', 'CAT001', 'ACTIVE', 'admin'),
(N'Thực phẩm', N'Các loại thực phẩm khô và tươi', 'CAT002', 'ACTIVE', 'admin'),
(N'Đồ gia dụng', N'Sản phẩm dùng trong gia đình', 'CAT003', 'ACTIVE', 'admin');

-- Thêm dữ liệu mẫu cho bảng Product
INSERT INTO Product (name, description, price, product_code, quantity, status, createdBy)
VALUES 
(N'Trà sữa trân châu', N'Trà sữa vị truyền thống với trân châu đen', 35000, 'PROD001', 100, 'ACTIVE', 'admin'),
(N'Bánh quy', N'Bánh quy hạt dẻ thơm ngon', 50000, 'PROD002', 50, 'ACTIVE', 'admin'),
(N'Nồi cơm điện', N'Nồi cơm điện đa chức năng', 1200000, 'PROD003', 20, 'ACTIVE', 'admin'),
(N'Cà phê sữa đá', N'Cà phê Việt Nam truyền thống', 30000, 'PROD004', 80, 'ACTIVE', 'admin');

-- Liên kết Product với Category qua bảng ProductCategory
-- Trà sữa trân châu thuộc danh mục Đồ uống
INSERT INTO ProductCategory (productId, categoryId, createdBy)
VALUES 
((SELECT id FROM Product WHERE product_code = 'PROD001'),
 (SELECT id FROM Category WHERE category_code = 'CAT001'), 'admin');

-- Bánh quy thuộc danh mục Thực phẩm
INSERT INTO ProductCategory (productId, categoryId, createdBy)
VALUES 
((SELECT id FROM Product WHERE product_code = 'PROD002'),
 (SELECT id FROM Category WHERE category_code = 'CAT002'), 'admin');

-- Nồi cơm điện thuộc danh mục Đồ gia dụng
INSERT INTO ProductCategory (productId, categoryId, createdBy)
VALUES 
((SELECT id FROM Product WHERE product_code = 'PROD003'),
 (SELECT id FROM Category WHERE category_code = 'CAT003'), 'admin');

-- Cà phê sữa đá thuộc danh mục Đồ uống
INSERT INTO ProductCategory (productId, categoryId, createdBy)
VALUES 
((SELECT id FROM Product WHERE product_code = 'PROD004'),
 (SELECT id FROM Category WHERE category_code = 'CAT001'), 'admin');

DROP TABLE IF EXISTS ProductCategory;

-- Sau đó mới xóa các bảng chính
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Category;