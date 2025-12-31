alter table companies
    add description varchar(1024) null;

alter table users
    add favourite_company_id int null;

alter table users
    add constraint users_companies_id_fk
        foreign key (favourite_company_id) references companies (id);