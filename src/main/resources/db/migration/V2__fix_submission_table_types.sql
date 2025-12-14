alter table submissions
    modify type enum ('INTERN', 'PLACED', 'INTERN_TO_PPO') not null;

alter table submissions
    modify cgpa decimal(10, 2) null;

alter table users
    modify degree enum ('BTECH', 'MTECH', 'MCA') not null;

alter table users
    modify year int not null;