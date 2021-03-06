create table realm (
  realm_id character varying(255),
  realm_name character varying(255),
  enabled boolean not null default true,
  sync_at timestamp with time zone,

  primary key (realm_id),
  unique (realm_name)
);

create table account (
  account_id uuid,
  account_name character varying(255) not null,
  password character varying(255),

  user_name character varying(255) not null,
  email character varying(255),

  locale character varying(255),
  realm_id character varying(255) not null,
  enabled boolean not null default true,

  primary key (account_id),
  unique (realm_id, account_name),
  foreign key (realm_id)
    references realm (realm_id) on delete cascade
);

create table notice (
  notice_id uuid,
  message text not null,
  start_at date,
  end_at date,
  update_at timestamp with time zone not null,

  primary key (notice_id)
);

create sequence group_generation_id_seq
    increment by 1
    start with 2
    no cycle;

create table group_generation (
  group_generation_id integer not null default nextval('group_generation_id_seq'),
  is_current boolean not null,

  primary key (group_generation_id)
);

create table group_transition (
  group_transition_id uuid,
  group_generation_id integer not null,
  group_name character varying(255) not null,
  parent_group_transition_id uuid,

  primary key (group_transition_id, group_generation_id),
  foreign key (group_generation_id)
    references group_generation (group_generation_id) on delete cascade
) partition by list (group_generation_id);

create table account_group_authority (
  account_id uuid,
  group_transition_id uuid,
  group_generation_id integer not null,
  role jsonb,

  primary key (account_id, group_transition_id, group_generation_id),
  foreign key (account_id)
    references account (account_id) on delete cascade
) partition by list (group_generation_id);

create table lesson (
  lesson_id uuid,
  group_transition_id uuid not null,
  slide_id character varying(255),
  primary key (lesson_id),
  unique (group_transition_id, slide_id)
);

create table study (
  study_id uuid,
  account_id uuid not null,
  slide_id character varying(255) not null,
  status character varying(255) not null,
  progress jsonb,
  progress_rate integer,
  answer jsonb,
  score jsonb,
  shuffled_question jsonb,
  start_at timestamp with time zone,
  end_at timestamp with time zone,

  primary key (study_id),
  unique (account_id, slide_id),
  foreign key (account_id)
    references account (account_id) on delete cascade
);

create materialized view group_transition_with_path as
  with recursive group_tree as (
    select group_transition.group_transition_id,
        group_transition.group_generation_id,
        group_transition.group_name,
        group_transition.parent_group_transition_id,
        1 as layer,
        '/' || group_transition.group_transition_id as path,
        '/' || group_transition.group_name as path_name
      from group_transition
      where group_transition.parent_group_transition_id is null
    union all
    select group_transition.group_transition_id,
        group_transition.group_generation_id,
        group_transition.group_name,
        group_transition.parent_group_transition_id,
        group_tree.layer + 1 as layer,
        group_tree.path || '/' || group_transition.group_transition_id as path,
        group_tree.path_name || '/' || group_transition.group_name as path_name
      from group_tree
        join group_transition on group_transition.parent_group_transition_id = group_tree.group_transition_id
	  		and ((group_transition.group_generation_id = group_tree.group_generation_id)
				 or (group_tree.group_generation_id = 0))
  )
  select * from group_tree;

create view current_account_group_authority as
  select account_group_authority.*
  from account_group_authority
    join group_generation
      on (group_generation.is_current = true)
      and (account_group_authority.group_generation_id = group_generation.group_generation_id);

insert into realm (realm_id, realm_name, enabled)
 values ('!system', 'Magic Lantern Server', true);

insert into group_generation (group_generation_id, is_current)
 values (0, true),
        (1, true);

create table group_0 partition of group_transition for values in (0);
create table group_1 partition of group_transition for values in (1);
create table account_group_authority_0 partition of account_group_authority for values in (0);
create table account_group_authority_1 partition of account_group_authority for values in (1);

insert into group_transition (group_transition_id, group_generation_id, group_name)
 values ('00000000-0000-0000-0000-000000000000', 0, '基本');

insert into account (account_id, account_name, user_name, password, realm_id)
 values ('00000000-0000-0000-0000-000000000000', 'admin', 'システム管理者', '{noop}password123', '!system');

insert into account_group_authority (account_id, group_transition_id, group_generation_id, role)
 values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 0, '["ADMIN","GROUP","SLIDE","LESSON","STUDY"]');