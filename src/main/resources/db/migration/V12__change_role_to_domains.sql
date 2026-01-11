CREATE TABLE domains (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NULL
);

INSERT INTO domains (id, name)
SELECT id, name FROM roles;

ALTER TABLE submissions
DROP FOREIGN KEY submissions_ibfk_3;

ALTER TABLE submissions
    CHANGE role_id domain_id INT,
    ADD COLUMN domain_description VARCHAR(100);

ALTER TABLE submissions
    ADD CONSTRAINT fk_submissions_domain
        FOREIGN KEY (domain_id) REFERENCES domains(id);

DROP TABLE roles;
