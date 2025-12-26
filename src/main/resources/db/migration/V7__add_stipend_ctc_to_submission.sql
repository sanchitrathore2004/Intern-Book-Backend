ALTER TABLE stipends
    ADD COLUMN submission_id INT;

ALTER TABLE stipends
    ADD CONSTRAINT fk_stipends_submission
        FOREIGN KEY (submission_id)
            REFERENCES submissions(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;


ALTER TABLE ctc_values
    ADD COLUMN submission_id INT;


ALTER TABLE ctc_values
    ADD CONSTRAINT fk_ctc_submission
        FOREIGN KEY (submission_id)
            REFERENCES submissions(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
