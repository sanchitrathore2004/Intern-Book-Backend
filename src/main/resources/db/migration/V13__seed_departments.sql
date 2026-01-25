INSERT INTO departments (id, name) VALUES
(1, 'CSE'),
(2, 'ECE'),
(3, 'EEE'),
(4, 'ICE'),
(5, 'MECH'),
(6, 'CIVIL'),
(7, 'CHEM'),
(8, 'MME'),
(9, 'PROD')
ON DUPLICATE KEY UPDATE
name = VALUES(name);