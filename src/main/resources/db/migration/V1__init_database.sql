create table companies
(
    id       int auto_increment
        primary key,
    name     varchar(255) not null,
    logo_url varchar(255) null,
    constraint name
        unique (name)
);

create index idx_company_name
    on companies (name);

create table departments
(
    id   int auto_increment
        primary key,
    name varchar(255) not null
);

create table roles
(
    id          int auto_increment
        primary key,
    name        varchar(255) not null,
    description text         null
);

create index idx_role_name
    on roles (name);

create table users
(
    roll_no       bigint                              not null
        primary key,
    name          varchar(255)                        not null,
    email         varchar(255)                        not null,
    degree        enum ('BTECH', 'MTECH', 'MCA')      null,
    department_id int                                 not null,
    year          int                                 null,
    created_at    timestamp default CURRENT_TIMESTAMP null,
    constraint email
        unique (email),
    constraint users_ibfk_1
        foreign key (department_id) references departments (id)
);

create table ctc_values
(
    id         int auto_increment
        primary key,
    user_id    bigint not null,
    company_id int    not null,
    ctc        int    not null,
    base_pay   int    null,
    constraint user_id
        unique (user_id, company_id),
    constraint ctc_values_ibfk_1
        foreign key (user_id) references users (roll_no),
    constraint ctc_values_ibfk_2
        foreign key (company_id) references companies (id)
);

create index company_id
    on ctc_values (company_id);

create table stipends
(
    id         int auto_increment
        primary key,
    user_id    bigint not null,
    company_id int    not null,
    stipend    int    not null,
    constraint user_id
        unique (user_id, company_id),
    constraint stipends_ibfk_1
        foreign key (user_id) references users (roll_no),
    constraint stipends_ibfk_2
        foreign key (company_id) references companies (id)
);

create index company_id
    on stipends (company_id);

create table submissions
(
    id             int auto_increment
        primary key,
    user_id        bigint                                     not null,
    company_id     int                                        not null,
    role_id        int                                        not null,
    type           enum ('INTERN', 'PLACED', 'INTERN_TO_PPO') null,
    benefits       text                                       null,
    experience     longtext                                   not null,
    mistakes       text                                       null,
    resources      text                                       null,
    cgpa           decimal(2, 2)                              null,
    club_positions text                                       null,
    approved       tinyint(1) default 0                       null,
    created_at     timestamp  default CURRENT_TIMESTAMP       null,
    constraint submissions_ibfk_1
        foreign key (user_id) references users (roll_no),
    constraint submissions_ibfk_2
        foreign key (company_id) references companies (id),
    constraint submissions_ibfk_3
        foreign key (role_id) references roles (id)
);

create table process_structure
(
    id            int auto_increment
        primary key,
    submission_id int      not null,
    ot_round      int      not null,
    pi_round      int      not null,
    start_date    datetime not null,
    end_date      datetime not null,
    constraint process_structure_ibfk_1
        foreign key (submission_id) references submissions (id)
            on delete cascade
);

create index submission_id
    on process_structure (submission_id);

create index idx_submission_company
    on submissions (company_id);

create index idx_submission_role
    on submissions (role_id);

create index idx_submission_user
    on submissions (user_id);

create index department_id
    on users (department_id);

