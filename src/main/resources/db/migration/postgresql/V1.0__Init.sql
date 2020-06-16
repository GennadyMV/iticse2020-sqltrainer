SET client_encoding = 'UTF8';

--
-- Tables
--

CREATE TABLE Database (
    id serial,
    created timestamp without time zone,
    description character varying(512),
    name character varying(64),
    sql_init_statements character varying(65536),
    student_id bigint
);

CREATE TABLE Exercise (
    id serial,
    created timestamp without time zone,
    handout character varying(1024),
    name character varying(64),
    sql_model_solution character varying(65536),
    database_id bigint,
    topic_id bigint,
    student_id bigint,
    disabled boolean
);

ALTER TABLE Exercise ALTER COLUMN disabled SET DEFAULT FALSE;

CREATE TABLE Exercise_attempt (
    id serial,
    correct boolean,
    created timestamp without time zone,
    sql_commands character varying(65536),
    exercise_id bigint,
    student_id bigint
);

CREATE TABLE Review (
    id serial,
    created timestamp without time zone,
    attempt_id bigint
);

CREATE TABLE Review_answers (
    review_id bigint NOT NULL,
    answer character varying(255),
    question character varying(255) NOT NULL
);

CREATE TABLE Student (
    id serial,
    created timestamp without time zone,
    username character varying(512)
);

CREATE TABLE Topic (
    id serial,
    created timestamp without time zone,
    description character varying(512),
    name character varying(64),
    rank integer
);

CREATE TABLE public.topic_mastery (
    id serial,
    mastery double precision,
    topic_id bigint,
    student_id bigint
);


--
-- Sequences
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Primary keys
--

ALTER TABLE ONLY public.database ADD CONSTRAINT database_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.exercise_attempt ADD CONSTRAINT exercise_attempt_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.exercise ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.review ADD CONSTRAINT review_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.student ADD CONSTRAINT student_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.topic_mastery ADD CONSTRAINT topic_mastery_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.topic ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- Foreign keys
--

ALTER TABLE ONLY public.exercise ADD CONSTRAINT exercise_to_student_fkey FOREIGN KEY (student_id) REFERENCES public.student(id);
ALTER TABLE ONLY public.exercise ADD CONSTRAINT exercise_to_database_fkey FOREIGN KEY (database_id) REFERENCES public.database(id);
ALTER TABLE ONLY public.exercise ADD CONSTRAINT exercise_to_topic_fkey FOREIGN KEY (topic_id) REFERENCES public.topic(id);

ALTER TABLE ONLY public.exercise_attempt ADD CONSTRAINT exercise_attempt_to_student_fkey FOREIGN KEY (student_id) REFERENCES public.student(id);
ALTER TABLE ONLY public.exercise_attempt ADD CONSTRAINT exercise_attempt_to_exercise_fkey FOREIGN KEY (exercise_id) REFERENCES public.exercise(id);

ALTER TABLE ONLY public.database ADD CONSTRAINT database_to_student_fkey FOREIGN KEY (student_id) REFERENCES public.student(id);

ALTER TABLE ONLY public.topic_mastery ADD CONSTRAINT topic_mastery_to_topic_fkey FOREIGN KEY (topic_id) REFERENCES public.topic(id);
ALTER TABLE ONLY public.topic_mastery ADD CONSTRAINT topic_mastery_to_student_fkey FOREIGN KEY (student_id) REFERENCES public.student(id);

ALTER TABLE ONLY public.review ADD CONSTRAINT review_to_exercise_attempt_fkey FOREIGN KEY (attempt_id) REFERENCES public.exercise_attempt(id);
ALTER TABLE ONLY public.review_answers ADD CONSTRAINT review_answers_to_review_pkey FOREIGN KEY (review_id) REFERENCES public.review(id);