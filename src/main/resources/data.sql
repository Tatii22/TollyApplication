-- Script de inicialización de roles
-- Este script se ejecuta automáticamente al iniciar la aplicación

-- Insertar roles iniciales si no existen (compatible con H2 y MySQL)
INSERT INTO roles (name, authority)
SELECT 'USER', 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');
INSERT INTO roles (name, authority)
SELECT 'ADMIN', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');
INSERT INTO roles (name, authority)
SELECT 'SUPPLIER', 'ROLE_SUPPLIER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SUPPLIER');
INSERT INTO roles (name, authority)
SELECT 'CLIENT', 'ROLE_CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CLIENT');

-- Insertar estados de usuario iniciales si no existen (compatible con H2 y MySQL)
INSERT INTO user_statuses (name)
SELECT 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM user_statuses WHERE name = 'ACTIVE');
INSERT INTO user_statuses (name)
SELECT 'INACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM user_statuses WHERE name = 'INACTIVE');
INSERT INTO user_statuses (name)
SELECT 'BLOCKED'
WHERE NOT EXISTS (SELECT 1 FROM user_statuses WHERE name = 'BLOCKED');

-- Insertar estados de reserva iniciales si no existen (compatible con H2 y MySQL)
INSERT INTO reservation_status (status_name)
SELECT 'RESERVED'
WHERE NOT EXISTS (SELECT 1 FROM reservation_status WHERE status_name = 'RESERVED');
INSERT INTO reservation_status (status_name)
SELECT 'IN_PROGRESS'
WHERE NOT EXISTS (SELECT 1 FROM reservation_status WHERE status_name = 'IN_PROGRESS');
INSERT INTO reservation_status (status_name)
SELECT 'FINISHED'
WHERE NOT EXISTS (SELECT 1 FROM reservation_status WHERE status_name = 'FINISHED');
INSERT INTO reservation_status (status_name)
SELECT 'CANCELLED'
WHERE NOT EXISTS (SELECT 1 FROM reservation_status WHERE status_name = 'CANCELLED');
INSERT INTO reservation_status (status_name)
SELECT 'IN_INCIDENT'
WHERE NOT EXISTS (SELECT 1 FROM reservation_status WHERE status_name = 'IN_INCIDENT');

INSERT INTO payment_status (name)
SELECT 'PENDIENTE_DEVOLUCION'
WHERE NOT EXISTS (SELECT 1 FROM payment_status WHERE name = 'PENDIENTE_DEVOLUCION');
INSERT INTO payment_status (name)
SELECT 'PAID'
WHERE NOT EXISTS (SELECT 1 FROM payment_status WHERE name = 'PAID');
INSERT INTO payment_status (name)
SELECT 'CANCELLED'
WHERE NOT EXISTS (SELECT 1 FROM payment_status WHERE name = 'CANCELLED');

-- Insertar estados de devolucion iniciales si no existen (compatible con H2 y MySQL)
INSERT INTO return_status (name)
SELECT 'PENDIENTE_DEVOLUCION'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'PENDIENTE_DEVOLUCION');
INSERT INTO return_status (name)
SELECT 'SENT'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'SENT');
INSERT INTO return_status (name)
SELECT 'RECEIVED'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'RECEIVED');
INSERT INTO return_status (name)
SELECT 'DAMAGED'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'DAMAGED');
INSERT INTO return_status (name)
SELECT 'CL_DAMAGED'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'CL_DAMAGED');
INSERT INTO return_status (name)
SELECT 'CL_INCOMPLETE'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'CL_INCOMPLETE');
INSERT INTO return_status (name)
SELECT 'SPP_INCOMPLETE'
WHERE NOT EXISTS (SELECT 1 FROM return_status WHERE name = 'SPP_INCOMPLETE');

-- Insertar estados de herramienta iniciales si no existen (compatible con H2 y MySQL)
INSERT INTO tool_status (name)
SELECT 'AVAILABLE'
WHERE NOT EXISTS (SELECT 1 FROM tool_status WHERE name = 'AVAILABLE');
INSERT INTO tool_status (name)
SELECT 'RENTED'
WHERE NOT EXISTS (SELECT 1 FROM tool_status WHERE name = 'RENTED');
INSERT INTO tool_status (name)
SELECT 'UNAVAILABLE'
WHERE NOT EXISTS (SELECT 1 FROM tool_status WHERE name = 'UNAVAILABLE');
INSERT INTO tool_status (name)
SELECT 'UNDER_REPAIR'
WHERE NOT EXISTS (SELECT 1 FROM tool_status WHERE name = 'UNDER_REPAIR');
