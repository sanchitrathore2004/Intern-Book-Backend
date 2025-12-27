alter table submissions
    add constraint unique_user_company_submission
        unique (user_id, company_id);

alter table stipends
    modify stipend decimal(10, 2) not null;

alter table users
    add profile_url varchar(1024) default 'https://robohash.org/Phunsi%20Kumar';