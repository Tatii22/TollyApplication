CREATE DATABASE IF NOT EXISTS tolly_db;
USE tolly_db;

-- =========================
-- USERS & SECURITY
-- =========================
CREATE TABLE user_statuses (
    user_status_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_status_id BIGINT NOT NULL,
    FOREIGN KEY (user_status_id) REFERENCES user_statuses(user_status_id)
) ENGINE=InnoDB;

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    authority VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE user_roles (
    user_id CHAR(36) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

-- =========================
-- CLIENTS & SUPPLIERS
-- =========================
CREATE TABLE clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    document_id VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE suppliers (
    id_supplier BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    identification VARCHAR(30) NOT NULL UNIQUE,
    contact_name VARCHAR(150),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================
-- TOOLS
-- =========================
CREATE TABLE categories (
    id_category BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE tool_status (
    id_tool_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE tools (
    id_tool BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    daily_price DOUBLE NOT NULL,
    total_quantity INT NOT NULL,
    available_quantity INT NOT NULL,
    id_tool_status BIGINT NOT NULL,
    id_supplier BIGINT NOT NULL,
    id_category BIGINT NOT NULL,
    FOREIGN KEY (id_tool_status) REFERENCES tool_status(id_tool_status),
    FOREIGN KEY (id_supplier) REFERENCES suppliers(id_supplier),
    FOREIGN KEY (id_category) REFERENCES categories(id_category)
) ENGINE=InnoDB;

CREATE TABLE tool_images (
    id_image BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tool BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_tool) REFERENCES tools(id_tool) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================
-- RESERVATIONS
-- =========================
CREATE TABLE reservation_status (
    id_reservation_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE reservations (
    id_reservation BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    id_reservation_status BIGINT NOT NULL,
    created_at DATE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (id_reservation_status) REFERENCES reservation_status(id_reservation_status)
) ENGINE=InnoDB;

CREATE TABLE reservation_detail (
    id_reservation_detail BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tool BIGINT NOT NULL,
    id_reservation BIGINT NOT NULL,
    daily_price DECIMAL(10,2) NOT NULL,
    rental_days INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_tool) REFERENCES tools(id_tool),
    FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================
-- RETURNS
-- =========================
CREATE TABLE return_status (
    id_return_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE returns (
    id_return BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_reservation BIGINT NOT NULL,
    id_client BIGINT NOT NULL,
    return_date DATE NOT NULL,
    id_return_status BIGINT NOT NULL,
    observations TEXT,
    FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation),
    FOREIGN KEY (id_client) REFERENCES clients(id),
    FOREIGN KEY (id_return_status) REFERENCES return_status(id_return_status)
) ENGINE=InnoDB;

CREATE TABLE return_detail (
    id_return_detail BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_return BIGINT NOT NULL,
    id_tool BIGINT NOT NULL,
    quantity INT NOT NULL,
    observations TEXT,
    FOREIGN KEY (id_return) REFERENCES returns(id_return) ON DELETE CASCADE,
    FOREIGN KEY (id_tool) REFERENCES tools(id_tool)
) ENGINE=InnoDB;

-- =========================
-- PAYMENTS & INVOICES
-- =========================
CREATE TABLE payment_status (
    id_payment_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE payments (
    id_payment BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_reservation BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP NULL DEFAULT NULL,
    id_payment_status BIGINT NOT NULL,
    FOREIGN KEY (id_reservation) REFERENCES reservations(id_reservation),
    FOREIGN KEY (id_payment_status) REFERENCES payment_status(id_payment_status)
) ENGINE=InnoDB;

CREATE TABLE invoices (
    id_invoice BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    id_payment BIGINT NOT NULL,
    issue_date TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_payment) REFERENCES payments(id_payment)
) ENGINE=InnoDB;

CREATE TABLE invoice_detail (
    id_invoice_detail BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_invoice BIGINT NOT NULL,
    id_tool BIGINT NOT NULL,
    daily_price DECIMAL(10,2) NOT NULL,
    rental_days INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_invoice) REFERENCES invoices(id_invoice) ON DELETE CASCADE,
    FOREIGN KEY (id_tool) REFERENCES tools(id_tool)
) ENGINE=InnoDB;

-- =========================
-- REFRESH TOKENS
-- =========================
CREATE TABLE refresh_tokens (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at DATETIME NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;
