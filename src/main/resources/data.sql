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
    ('RESERVED'),
    ('IN_PROGRESS'),
    ('FINISHED'),
    ('CANCELLED'),
    ('IN_INCIDENT');

INSERT IGNORE INTO payment_status (name) 
VALUES 
    ('PENDING'),
    ('PAID'),
    ('CANCELLED');

-- Insertar estados de devolucion iniciales si no existen
INSERT IGNORE INTO return_status (name)
VALUES
    ('PENDING'),
    ('SENT'),
    ('RECEIVED'),
    ('DAMAGED');

-- Insertar estados de herramienta iniciales si no existen
INSERT IGNORE INTO tool_status (name)
VALUES
    ('AVAILABLE'),
    ('RENTED'),
    ('UNAVAILABLE'),
    ('UNDER_REPAIR'),
    ('UNAVAILABLE');
