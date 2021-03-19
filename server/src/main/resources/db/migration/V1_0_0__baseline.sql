create table account (
  username character varying(255),
  password character varying(255) not null,
  role character varying(255) not null,
  primary key (username)
);

create table topic (
  topic_id bigserial,
  create_at timestamp not null,
  update_at timestamp,

  title character varying(255) not null,
  filename character varying(255) not null,
  primary key (topic_id)
);
