-- Script de inicialización de roles
-- Este script se ejecuta automáticamente al iniciar la aplicación

-- Insertar roles iniciales si no existen
-- Para MySQL con UUID como CHAR(36)
INSERT IGNORE INTO roles (id, name, authority)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'USER', 'ROLE_USER'),
    ('550e8400-e29b-41d4-a716-446655440002', 'ADMIN', 'ROLE_ADMIN'),
    ('550e8400-e29b-41d4-a716-446655440003', 'SUPPLIER', 'ROLE_SUPPLIER'),
    ('550e8400-e29b-41d4-a716-446655440004', 'CLIENT', 'ROLE_CLIENT');

-- Insertar estados de usuario iniciales si no existen
INSERT IGNORE INTO user_statuses (user_status_id, name)
VALUES
    ('550e8400-e29b-41d4-a716-446655440005', 'ACTIVE'),
    ('550e8400-e29b-41d4-a716-446655440006', 'INACTIVE'),
    ('550e8400-e29b-41d4-a716-446655440007', 'BLOCKED');
