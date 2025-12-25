alter table stipends
    modify stipend decimal(10, 2) not null;

alter table ctc_values
    modify ctc decimal(10, 2) not null,
    modify base_pay decimal(10, 2) null;