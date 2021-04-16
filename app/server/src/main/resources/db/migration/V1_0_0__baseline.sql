create table account (
  account_id uuid,
  account_name character varying(255) not null,
  password character varying(255),

  user_name character varying(255) not null,
  email character varying(255),

  locale character varying(255),
  realm_id character varying(255),
  enabled boolean not null default true,

  primary key (account_id),
  unique (realm_id, account_name)
);

create table realm (
  realm_id character varying(255),
  realm_name character varying(255),
  enabled boolean not null default true,
  sync_at timestamp,

  primary key (realm_id)
);

create table group_generation (
  group_generation_id uuid,
  start_date date,

  primary key (group_generation_id)
);

create table group_origin (
  group_origin_id uuid,

  primary key (group_origin_id)
);

create table group_transition (
  group_transition_id uuid,
  group_generation_id uuid not null,
  group_origin_id uuid not null,
  group_name character varying(255) not null,

  primary key (group_transition_id),
  foreign key (group_generation_id)
    references group_generation (group_generation_id) on delete cascade,
  foreign key (group_origin_id)
    references group_origin (group_origin_id) on delete cascade
);

create table account_group_authority (
  account_id uuid,
  group_transition_id uuid,
  role character varying(255) not null,

  primary key (account_id, group_transition_id),
  foreign key (account_id)
    references account (account_id) on delete cascade,
  foreign key (group_transition_id)
    references group_transition (group_transition_id) on delete cascade
);

create view group_generation_period as
  select group_generation_id, start_date, lead( start_date ) OVER( ORDER BY start_date ) - 1 as end_date
  from group_generation;

create materialized view current_group_transition as
  select group_transition.*
  from group_transition
    left join group_generation_period on group_transition.group_generation_id = group_generation_period.group_generation_id
      and ((start_date < now() and (end_date is null or now() < end_date)) or start_date is null);

create materialized view current_account_group_authority as
  select account_group_authority.*, current_group_transition.group_origin_id
  from account_group_authority
    left join current_group_transition on account_group_authority.group_transition_id = current_group_transition.group_transition_id;

create view user_aggregate as
  select account.*, current_account_group_authority.group_origin_id, current_account_group_authority.role
  from account
    left join current_account_group_authority on current_account_group_authority.account_id = account.account_id;

insert into group_generation (group_generation_id) values ('00000000-0000-0000-0000-000000000000');
insert into group_origin (group_origin_id) values ('00000000-0000-0000-0000-000000000000');
insert into group_transition (group_transition_id, group_generation_id, group_origin_id, group_name) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '全体');

insert into account (account_id, account_name, user_name, password) values ('00000000-0000-0000-0000-000000000000', 'admin', 'システム管理者', '{noop}password123');
insert into account_group_authority (account_id, group_transition_id, role) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'ADMIN');