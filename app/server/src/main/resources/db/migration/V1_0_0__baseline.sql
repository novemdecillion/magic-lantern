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
  sync_at timestamp with time zone,

  primary key (realm_id)
);

create table group_generation (
  group_generation_id uuid,
  start_date timestamp with time zone,

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
  parent_group_transition_id uuid,

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

  primary key (account_id, group_transition_id, role),
  foreign key (account_id)
    references account (account_id) on delete cascade,
  foreign key (group_transition_id)
    references group_transition (group_transition_id) on delete cascade
);

-- create table slide (
--   slide_id character varying(255),
--   primary key (slide_id)
-- );

create table lesson (
  lesson_id uuid,
  group_origin_id uuid not null,
  slide_id character varying(255),
  primary key (lesson_id),
  unique (group_origin_id, slide_id),
  foreign key (group_origin_id)
    references group_origin (group_origin_id) on delete cascade
--  foreign key (slide_id)
--    references slide (slide_id) on delete cascade
);

create table study (
  study_id uuid,
  account_id uuid not null,
  slide_id character varying(255) not null,
  progress text,
  progress_rate integer,
  answer text,
  score integer,
  start_at timestamp with time zone,
  end_at timestamp with time zone,

  primary key (study_id),
  unique (account_id, slide_id),
  foreign key (account_id)
    references account (account_id) on delete cascade
--  foreign key (slide_id)
--    references slide (slide_id) on delete cascade
);

create view group_generation_period as
  select group_generation_id, start_date, lead( start_date ) OVER( ORDER BY start_date ) as end_date
  from group_generation;

create materialized view current_group_transition as
  with recursive
  current_group as (
    select group_transition.group_transition_id,
        group_transition.group_generation_id,
        group_transition.group_origin_id,
        group_transition.group_name,
        group_transition.parent_group_transition_id
      from group_transition
        inner join group_generation_period on group_transition.group_generation_id = group_generation_period.group_generation_id and (group_generation_period.start_date is null or group_generation_period.start_date < now()) and (group_generation_period.end_date is null or now() < group_generation_period.end_date)
  ),
  group_tree as (
    select current_group.group_transition_id,
        current_group.group_generation_id,
        current_group.group_origin_id,
        current_group.group_name,
        current_group.parent_group_transition_id,
        1 as layer,
        '/' || current_group.group_transition_id as path,
        '/' || current_group.group_name as path_name
      from current_group
      where current_group.parent_group_transition_id is null
    union all
    select current_group.group_transition_id,
        current_group.group_generation_id,
        current_group.group_origin_id,
        current_group.group_name,
        current_group.parent_group_transition_id,
        group_tree.layer + 1 as layer,
        group_tree.path || '/' || current_group.group_transition_id as path,
        group_tree.path_name || '/' || current_group.group_name as path_name
      from group_tree
        inner join current_group on current_group.parent_group_transition_id = group_tree.group_transition_id
  )
  select * from group_tree;

create materialized view current_account_group_authority as
  select account_group_authority.*, current_group_transition.group_origin_id
  from account_group_authority
    left join current_group_transition on account_group_authority.group_transition_id = current_group_transition.group_transition_id;

create view user_aggregate as
  select account.*, current_account_group_authority.group_origin_id, current_account_group_authority.role
  from account
    left join current_account_group_authority on current_account_group_authority.account_id = account.account_id;

insert into group_generation (group_generation_id)
 values ('00000000-0000-0000-0000-000000000000'),
        ('00000000-0000-0000-0000-000000000001');
insert into group_origin (group_origin_id)
 values ('00000000-0000-0000-0000-000000000000');
insert into group_transition (group_transition_id, group_generation_id, group_origin_id, group_name)
 values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '全体');

insert into account (account_id, account_name, user_name, password)
 values ('00000000-0000-0000-0000-000000000000', 'admin', 'システム管理者', '{noop}password123');
insert into account_group_authority (account_id, group_transition_id, role)
 values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'ADMIN'),
        ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'GROUP'),
        ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'SLIDE'),
        ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'LESSON'),
        ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'STUDY');

-- テスト用
insert into group_origin (group_origin_id)
 values ('00000000-0000-0000-0000-000000000001'),
        ('00000000-0000-0000-0000-000000000002'),
        ('00000000-0000-0000-0000-000000000003'),
        ('00000000-0000-0000-0000-000000000004'),
        ('00000000-0000-0000-0000-000000000005');

insert into group_transition (group_transition_id, group_generation_id, group_origin_id, group_name, parent_group_transition_id)
 values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'A部', '00000000-0000-0000-0000-000000000000'),
        ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 'B部', '00000000-0000-0000-0000-000000000000'),
        ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'A1課', '00000000-0000-0000-0000-000000000001'),
        ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004', 'A2課', '00000000-0000-0000-0000-000000000001'),
        ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000005', 'B1課', '00000000-0000-0000-0000-000000000002');

insert into account (account_id, account_name, user_name, password)
 values ('00000000-0000-0000-0000-000000000010', 'user0001', '事業部長', '{noop}password123');
insert into account_group_authority (account_id, group_transition_id, role)
 values ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000000', 'NONE'),
        ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000001', 'GROUP'),
        ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000002', 'GROUP'),
        ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000004', 'GROUP');
