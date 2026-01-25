-- ============================================
-- DEV SEED SCRIPT
-- Truncate and insert users and submissions
-- Safe to run multiple times in development
-- ============================================

-- Disable foreign key checks to allow truncation
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables
TRUNCATE TABLE submissions;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- Insert Users
-- ============================================
INSERT INTO users
(roll_no, name, email, degree, department_id, year, created_at, profile_url, user_role, is_anonymous, favourite_company_id)
VALUES
    (106123122, 'Shreyash Verma', '106123122@nitt.edu', 'BTECH', 1, 3, CURRENT_TIMESTAMP, 'https://robohash.org/106123122', 'USER', 0, NULL),
    (110123099, 'Sanchit Rathore', '110123099@nitt.edu', 'BTECH', 4, 3, CURRENT_TIMESTAMP, 'https://robohash.org/110123099', 'USER', 0, NULL),
    (106123032, 'Daksh Tulsyan', '106123032@nitt.edu', 'BTECH', 1, 3, CURRENT_TIMESTAMP, 'https://robohash.org/106123032', 'USER', 0, NULL),
    (107122001, 'Aditi Sharma', '107122001@nitt.edu', 'BTECH', 1, 4, CURRENT_TIMESTAMP, 'https://robohash.org/107122001', 'USER', 0, NULL),
    (108123045, 'Rohan Das', '108123045@nitt.edu', 'BTECH', 3, 3, CURRENT_TIMESTAMP, 'https://robohash.org/108123045', 'USER', 0, NULL),
    (205123010, 'Sneha Kapoor', '205123010@nitt.edu', 'MCA', 4, 2, CURRENT_TIMESTAMP, 'https://robohash.org/205123010', 'USER', 0, NULL),
    (110121099, 'Vikram Singh', '110121099@nitt.edu', 'BTECH', 5, 5, CURRENT_TIMESTAMP, 'https://robohash.org/110121099', 'USER', 0, NULL),
    (111124022, 'Priyanka Nair', '111124022@nitt.edu', 'BTECH', 6, 1, CURRENT_TIMESTAMP, 'https://robohash.org/111124022', 'USER', 0, NULL),
    (212123005, 'Arjun Reddy', '212123005@nitt.edu', 'MTECH', 7, 2, CURRENT_TIMESTAMP, 'https://robohash.org/212123005', 'USER', 0, NULL),
    (114122067, 'Ishaan Malhotra', '114122067@nitt.edu', 'BTECH', 8, 4, CURRENT_TIMESTAMP, 'https://robohash.org/114122067', 'USER', 0, NULL),
    (205123015, 'Kavya Iyer', '205123015@nitt.edu', 'MCA', 9, 2, CURRENT_TIMESTAMP, 'https://robohash.org/205123015', 'USER', 0, NULL),
    (106123150, 'Rahul Gupta', '106123150@nitt.edu', 'BTECH', 1, 3, CURRENT_TIMESTAMP, 'https://robohash.org/106123150', 'USER', 0, NULL),
    (110122040, 'Ananya Bose', '110122040@nitt.edu', 'BTECH', 2, 4, CURRENT_TIMESTAMP, 'https://robohash.org/110122040', 'USER', 0, NULL),
    (212124001, 'Karthik S.', '212124001@nitt.edu', 'MTECH', 3, 1, CURRENT_TIMESTAMP, 'https://robohash.org/212124001', 'USER', 0, NULL),
    (108123088, 'Meera Pillai', '108123088@nitt.edu', 'BTECH', 4, 3, CURRENT_TIMESTAMP, 'https://robohash.org/108123088', 'USER', 0, NULL),
    (111121005, 'Sahil Khan', '111121005@nitt.edu', 'BTECH', 5, 5, CURRENT_TIMESTAMP, 'https://robohash.org/111121005', 'USER', 0, NULL),
    (205124030, 'Tanvi Joshi', '205124030@nitt.edu', 'MCA', 6, 1, CURRENT_TIMESTAMP, 'https://robohash.org/205124030', 'USER', 0, NULL),
    (107123090, 'Yash Vardhan', '107123090@nitt.edu', 'BTECH', 7, 3, CURRENT_TIMESTAMP, 'https://robohash.org/107123090', 'USER', 0, NULL),
    (212122012, 'Diya Saxena', '212122012@nitt.edu', 'MTECH', 8, 4, CURRENT_TIMESTAMP, 'https://robohash.org/212122012', 'USER', 0, NULL),
    (114123002, 'Aman Tiwari', '114123002@nitt.edu', 'BTECH', 9, 3, CURRENT_TIMESTAMP, 'https://robohash.org/114123002', 'USER', 0, NULL),
    (106124055, 'Riya Chawla', '106124055@nitt.edu', 'BTECH', 1, 1, CURRENT_TIMESTAMP, 'https://robohash.org/106124055', 'USER', 0, NULL),
    (205122008, 'Sameer Deshmukh', '205122008@nitt.edu', 'MCA', 2, 4, CURRENT_TIMESTAMP, 'https://robohash.org/205122008', 'USER', 0, NULL);

-- ============================================
-- Insert Submissions
-- ============================================
INSERT INTO submissions
(user_id, company_id, domain_id, type, experience, resources, cgpa, benefits, mistakes, club_positions, approved, created_at, is_offensive, is_fraud, like_count, domain_description)
VALUES
    (106123122, 1, 1, 'INTERN', 'Worked on Spring Boot REST APIs.', 'Spring Docs', 9.0, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Software / Backend'),
    (107122001, 2, 2, 'PLACED', 'Strong DSA and system design rounds.', 'LeetCode', 8.8, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Systems / Algorithms'),
    (108123045, 3, 1, 'INTERN', 'ML preprocessing and evaluation.', 'Kaggle', 9.2, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Software / Development'),
    (205123010, 4, 3, 'PLACED', 'VLSI and digital logic interviews.', 'NPTEL', 7.9, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Core / Hardware'),
    (110121099, 5, 5, 'INTERN', 'React UI and UX improvements.', 'MDN', 8.1, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Design / Frontend'),
    (111124022, 6, 4, 'PLACED', 'Product sense and case studies.', 'Decode & Conquer', 8.9, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Product / Management'),
    (212123005, 7, 1, 'INTERN', 'Spring Boot + JPA backend.', 'Baeldung', 9.5, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Java Backend'),
    (114122067, 8, 2, 'PLACED', 'Algorithm-heavy rounds.', 'Codeforces', 8.3, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Competitive Programming'),
    (205123015, 9, 1, 'INTERN', 'Built analytics dashboards.', 'Google Analytics', 7.5, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Analytics / Dashboards'),
    (106123150, 10, 5, 'PLACED', 'UX-focused frontend interviews.', 'Figma', 8.7, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'UX / Frontend'),
    (110122040, 11, 1, 'INTERN', 'SQL optimization tasks.', 'Postgres Docs', 8.0, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Database / Backend'),
    (212124001, 12, 3, 'INTERN', 'Firmware and MCU work.', 'Datasheets', 8.6, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Embedded Systems'),
    (108123088, 13, 1, 'PLACED', 'CUDA and parallel computing.', 'CUDA Docs', 9.1, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'GPU / Systems'),
    (111121005, 14, 3, 'INTERN', 'SoC design fundamentals.', 'TI Docs', 8.4, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Core Electronics'),
    (205124030, 15, 4, 'PLACED', 'Consulting case interviews.', 'Case in Point', 9.3, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Consulting / Strategy'),
    (107123090, 16, 2, 'INTERN', 'Advanced graph problems.', 'CP Handbook', 7.9, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Algorithms / Graphs'),
    (212122012, 17, 1, 'PLACED', 'Scalability and distributed design.', 'MIT OCW', 8.6, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Distributed Systems'),
    (114123002, 18, 5, 'INTERN', 'User research and testing.', 'NNGroup', 9.9, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'UX Research'),
    (106124055, 19, 1, 'PLACED', 'Native Apple ecosystem development.', 'Apple Docs', 7.2, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'iOS / macOS'),
    (205122008, 20, 2, 'PLACED', 'Spark and Hadoop ecosystem.', 'Spark Docs', 8.8, NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, 0, 0, 0, 'Big Data');
