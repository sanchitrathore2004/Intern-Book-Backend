create table favourites
(
    user_id       bigint not null,
    submission_id int    not null,
    constraint favourites_submissions_id_fk
        foreign key (submission_id) references submissions (id),
    constraint favourites_users_roll_no_fk
        foreign key (user_id) references users (roll_no)
);

create unique index favourites_submission_id_user_id_uindex
    on favourites (submission_id, user_id);

create index favourites_user_id_index
    on favourites (user_id);

create table reports
(
    id            int auto_increment primary key,
    submission_id int not null,
    user_id       bigint not null,
    reason        enum ('OFFENSIVE', 'FRAUD') not null,
    created_at    timestamp default CURRENT_TIMESTAMP,

    constraint unique_report unique (submission_id, user_id),

    foreign key (submission_id) references submissions (id) on delete cascade,
    foreign key (user_id) references users (roll_no) on delete cascade
);

create table likes
(
    submission_id int    not null,
    user_id       bigint not null,

    constraint unique_like unique (submission_id, user_id),
    constraint likes__roll_no_fk
        foreign key (user_id) references users (roll_no),
    constraint likes_submissions_id_fk
        foreign key (submission_id) references submissions (id)
);

-- <> After the count of reports for a particular submission crosses threshold
--    then mark it (offensive/fraud) in the submission table
-- <> When somebody likes, save that like in the likes table then update the like_count
--    in the submissions table
alter table submissions
    add column is_offensive tinyint(1) default 0,
    add column is_fraud tinyint(1) default 0,
    add column like_count int default 0;