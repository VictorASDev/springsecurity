-- src/main/resources/schema.sql
CREATE TABLE IF NOT EXISTS tb_roles (
  role_id INT PRIMARY KEY,
  name VARCHAR(255)
);


INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1, "ADMIN");
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2, "BASIC");