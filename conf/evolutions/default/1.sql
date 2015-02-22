# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  location                  varchar(255),
  number_of_employees       integer,
  constraint pk_company primary key (id))
;

create table degree (
  id                        bigint auto_increment not null,
  date_started              timestamp,
  date_ended                timestamp,
  name                      varchar(255),
  field_of_study            varchar(255),
  grade                     varchar(255),
  person_id                 bigint,
  school_id                 bigint,
  constraint pk_degree primary key (id))
;

create table person (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  administrator             boolean,
  email                     varchar(255),
  password                  varchar(255),
  linked_in_username        varchar(255),
  linked_in_password        varchar(255),
  linked_in_url             varchar(255),
  linked_in_token           varchar(255),
  facebook_token            varchar(255),
  cv_name                   varchar(255),
  constraint pk_person primary key (id))
;

create table position (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  date_started              timestamp,
  date_ended                timestamp,
  constraint pk_position primary key (id))
;

create table project (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  date_started              timestamp,
  date_ended                timestamp,
  company_id                bigint,
  constraint pk_project primary key (id))
;

create table school (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  date_created              timestamp,
  location                  varchar(255),
  constraint pk_school primary key (id))
;

create table tag (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_tag primary key (id))
;


create table position_company (
  position_id                    bigint not null,
  company_id                     bigint not null,
  constraint pk_position_company primary key (position_id, company_id))
;

create table position_person (
  position_id                    bigint not null,
  person_id                      bigint not null,
  constraint pk_position_person primary key (position_id, person_id))
;

create table project_person (
  project_id                     bigint not null,
  person_id                      bigint not null,
  constraint pk_project_person primary key (project_id, person_id))
;

create table tag_person (
  tag_id                         bigint not null,
  person_id                      bigint not null,
  constraint pk_tag_person primary key (tag_id, person_id))
;
alter table degree add constraint fk_degree_person_1 foreign key (person_id) references person (id) on delete restrict on update restrict;
create index ix_degree_person_1 on degree (person_id);
alter table degree add constraint fk_degree_school_2 foreign key (school_id) references school (id) on delete restrict on update restrict;
create index ix_degree_school_2 on degree (school_id);
alter table project add constraint fk_project_company_3 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_project_company_3 on project (company_id);



alter table position_company add constraint fk_position_company_position_01 foreign key (position_id) references position (id) on delete restrict on update restrict;

alter table position_company add constraint fk_position_company_company_02 foreign key (company_id) references company (id) on delete restrict on update restrict;

alter table position_person add constraint fk_position_person_position_01 foreign key (position_id) references position (id) on delete restrict on update restrict;

alter table position_person add constraint fk_position_person_person_02 foreign key (person_id) references person (id) on delete restrict on update restrict;

alter table project_person add constraint fk_project_person_project_01 foreign key (project_id) references project (id) on delete restrict on update restrict;

alter table project_person add constraint fk_project_person_person_02 foreign key (person_id) references person (id) on delete restrict on update restrict;

alter table tag_person add constraint fk_tag_person_tag_01 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

alter table tag_person add constraint fk_tag_person_person_02 foreign key (person_id) references person (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists company;

drop table if exists position_company;

drop table if exists degree;

drop table if exists person;

drop table if exists position_person;

drop table if exists project_person;

drop table if exists tag_person;

drop table if exists position;

drop table if exists project;

drop table if exists school;

drop table if exists tag;

SET REFERENTIAL_INTEGRITY TRUE;

