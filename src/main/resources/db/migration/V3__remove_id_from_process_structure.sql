ALTER TABLE process_structure
    MODIFY COLUMN id INT NOT NULL;

ALTER TABLE process_structure
DROP PRIMARY KEY;

ALTER TABLE process_structure
DROP COLUMN id;

ALTER TABLE process_structure
    ADD PRIMARY KEY (submission_id);