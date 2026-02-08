create table user_request
(
    id           int auto_increment
        primary key,
    user_id      bigint       not null,
    company_name varchar(255) not null,
    constraint user_request_users_roll_no_fk
        foreign key (user_id) references users (roll_no)
);

