-- 创建用户表
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入示例用户数据
INSERT INTO users (username, password, email) VALUES
                                                  ('user1', 'password1', 'user1@example.com'),
                                                  ('user2', 'password2', 'user2@example.com'),
                                                  ('user3', 'password3', 'user3@example.com');

-- 根据用户名和密码验证登录
SELECT COUNT(*) FROM users WHERE username = 'user1' AND password = 'password1';
