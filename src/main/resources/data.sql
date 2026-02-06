-- Script de inicialización de roles
-- Este script se ejecuta automáticamente al iniciar la aplicación

-- Insertar roles iniciales si no existen
INSERT IGNORE INTO roles (name, authority)
VALUES
    ('USER', 'ROLE_USER'),
    ('ADMIN', 'ROLE_ADMIN'),
    ('SUPPLIER', 'ROLE_SUPPLIER'),
    ('CLIENT', 'ROLE_CLIENT');

-- Insertar estados de usuario iniciales si no existen
INSERT IGNORE INTO user_statuses (name)
VALUES
    ('ACTIVE'),
    ('INACTIVE'),
    ('BLOCKED');

-- Insertar estados de reserva iniciales si no existen
INSERT IGNORE INTO reservation_status (status_name)
VALUES
    ('IN_PROGRESS'),
    ('FINISHED'),
    ('CANCELLED');

INSERT IGNORE INTO payment_status (name) 
VALUES 
    ('PENDING'),
    ('PAID');
