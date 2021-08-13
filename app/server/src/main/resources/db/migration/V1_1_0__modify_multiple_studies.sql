alter table study add column index integer not null default 0;
alter table study drop constraint study_account_id_slide_id_key;
alter table study add unique(account_id, slide_id, index);