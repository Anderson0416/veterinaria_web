INSERT INTO roles (id, nombre) VALUES 
(1, 'ADMIN'),
(2, 'REC'),
(3, 'VET');
INSERT INTO usuarios (
    `id`, 
    `apellido`, 
    `contraseña`, 
    `fecha_contrato`, 
    `fecha_nacimiento`, 
    `login`, 
    `nombre`, 
    `sexo`, 
    `telefono`, 
    `tipo_id`, 
    `rol_id`
) VALUES (
    '1', 
    'ADMIN', 
    '$2a$10$fbJGua/XlmvdQXcxZV2E6.bD9JEEAD8kt.6Q/AZk3LtYEhHTHkRwq', 
    '2024-01-01',
    '1990-01-01', 
    'ADMIN', 
    'ADMIN', 
    'ADMIN', 
    'ADMIN', 
    'ADMIN', 
    '1'
);