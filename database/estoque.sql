CREATE DATABASE IF NOT EXISTS estoque_facil
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE estoque_facil;

CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(60) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0
);
