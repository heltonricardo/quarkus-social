CREATE DATABASE quarkus-social;

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    age integer,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS posts
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    datetime timestamp without time zone,
    text character varying(255) COLLATE pg_catalog."default",
    user_id bigint,
    CONSTRAINT posts_pkey PRIMARY KEY (id),
    CONSTRAINT fkqwy1e63idnvjerwvc47tq3k5 FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
