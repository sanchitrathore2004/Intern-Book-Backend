alter table users
    add user_role enum ('USER', 'ADMIN', 'MENTOR') default 'USER' not null;

alter table users
    add is_anonymous boolean default false not null;
