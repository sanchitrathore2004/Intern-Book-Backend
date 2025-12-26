alter table submissions
    add constraint unique_user_company_submission
        unique (user_id, company_id);

alter table stipends
    modify stipend decimal(10, 2) not null;