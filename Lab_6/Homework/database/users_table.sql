DROP DATABASE IF EXISTS student_management;
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_code VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    major VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO students (student_code, full_name, email, major) VALUES
('SV010', 'Hoang Thuy Linh', 'linh.hoang@example.com', 'Computer Science'),
('SV011', 'Dang Van Lam', 'lam.dang@example.com', 'Information Technology'),
('SV012', 'Doan Van Hau', 'hau.doan@example.com', 'Software Engineering'),
('SV013', 'Nguyen Quang Hai', 'hai.nguyen@example.com', 'Business Administration'),
('SV014', 'Phan Van Duc', 'duc.phan@example.com', 'Computer Science'),
('SV015', 'Que Ngoc Hai', 'hai.que@example.com', 'Information Technology'),
('SV016', 'Bui Tien Dung', 'dung.bui@example.com', 'Software Engineering'),
('SV017', 'Nguyen Cong Phuong', 'phuong.nguyen@example.com', 'Business Administration'),
('SV018', 'Tran Dinh Trong', 'trong.tran@example.com', 'Computer Science'),
('SV019', 'Luong Xuan Truong', 'truong.luong@example.com', 'Information Technology'),
('SV020', 'Ha Duc Chinh', 'chinh.ha@example.com', 'Software Engineering'),
('SV021', 'Nguyen Tuan Anh', 'anh.nguyen@example.com', 'Business Administration'),
('SV022', 'Vu Van Thanh', 'thanh.vu@example.com', 'Computer Science'),
('SV023', 'Nguyen Phong Hong Duy', 'duy.nguyen@example.com', 'Information Technology'),
('SV024', 'Do Duy Manh', 'manh.do@example.com', 'Software Engineering'),
('SV025', 'Pham Duc Huy', 'huy.pham@example.com', 'Business Administration'),
('SV026', 'Nguyen Tien Linh', 'linh.nguyen@example.com', 'Computer Science'),
('SV027', 'Ho Tan Tai', 'tai.ho@example.com', 'Information Technology'),
('SV028', 'Nguyen Thanh Chung', 'chung.nguyen@example.com', 'Software Engineering'),
('SV029', 'Bui Hoang Viet Anh', 'anh.bui@example.com', 'Business Administration');

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') DEFAULT 'user',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Insert sample users (password is 'password123' hashed with BCrypt)
INSERT INTO users (username, password, full_name, role) VALUES
('admin', '$2a$10$Ki2I7Ot9IPFoiRjFDf51duKxy6cfCXu4taXpbttBabZsF4trDPaki', 'Admin User', 'admin'),
('john', '$2a$10$Ki2I7Ot9IPFoiRjFDf51duKxy6cfCXu4taXpbttBabZsF4trDPaki', 'John Doe', 'user'),
('jane', '$2a$10$Ki2I7Ot9IPFoiRjFDf51duKxy6cfCXu4taXpbttBabZsF4trDPaki', 'Jane Smith', 'user');

CREATE TABLE remember_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_expires (expires_at)
);
