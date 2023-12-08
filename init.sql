SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE DATABASE rinha;

\c rinha

create table pessoa
(
    id         uuid not null
        primary key,
    apelido    varchar(32)
        unique,
    nome       varchar(32),
    busca      tsvector,
    nascimento varchar(255),
    stack      varchar(255)
);

alter table pessoa
    owner to pgadmin;

create index busca_idx
    on pessoa (busca);
