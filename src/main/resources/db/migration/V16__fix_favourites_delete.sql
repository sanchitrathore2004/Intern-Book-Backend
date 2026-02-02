alter table favourites
drop foreign key favourites_submissions_id_fk;

alter table favourites
    add constraint favourites_submissions_id_fk
        foreign key (submission_id) references submissions (id)
            on delete cascade;

alter table favourites
drop foreign key favourites_users_roll_no_fk;

alter table favourites
    add constraint favourites_users_roll_no_fk
        foreign key (user_id) references users (roll_no)
            on delete cascade;