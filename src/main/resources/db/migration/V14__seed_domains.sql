INSERT INTO domains (id, name) VALUES
(1, 'Software'),
(2, 'Core'),
(3, 'Analytics'),
(4, 'Management'),
(5, 'Electronics')
ON DUPLICATE KEY UPDATE
name = VALUES(name);