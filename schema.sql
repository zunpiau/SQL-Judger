--
-- PostgreSQL database dump
--

-- Dumped from database version 10.8 (Ubuntu 10.8-0ubuntu0.18.04.1)
-- Dumped by pg_dump version 10.8 (Ubuntu 10.8-0ubuntu0.18.04.1)

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

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: citext; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;


--
-- Name: EXTENSION citext; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';


--
-- Name: set_to_now(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.set_to_now() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.set_to_now() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: answer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.answer
(
    id                 bigint NOT NULL,
    answersheet_id     bigint,
    exercise_config_id bigint NOT NULL,
    inputdata          character varying(2048),
    inputsql           character varying(2048),
    score              integer
);


ALTER TABLE public.answer
    OWNER TO postgres;

--
-- Name: answer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answer_id_seq
    OWNER TO postgres;

--
-- Name: answer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.answer_id_seq OWNED BY public.answer.id;


--
-- Name: answersheet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.answersheet
(
    id             bigint NOT NULL,
    exam_id        bigint NOT NULL,
    score          integer,
    student_number bigint NOT NULL
);


ALTER TABLE public.answersheet
    OWNER TO postgres;

--
-- Name: answersheet_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.answersheet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answersheet_id_seq
    OWNER TO postgres;

--
-- Name: answersheet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.answersheet_id_seq OWNED BY public.answersheet.id;


--
-- Name: clazz; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clazz
(
    id    bigint                NOT NULL,
    grade integer               NOT NULL,
    name  character varying(16) NOT NULL
);


ALTER TABLE public.clazz
    OWNER TO postgres;

--
-- Name: clazz_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clazz_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.clazz_id_seq
    OWNER TO postgres;

--
-- Name: clazz_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clazz_id_seq OWNED BY public.clazz.id;


--
-- Name: exam; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exam
(
    id           bigint                 NOT NULL,
    endtime      bigint,
    starttime    bigint,
    status       integer,
    title        character varying(255) NOT NULL,
    teaching_id  bigint                 NOT NULL,
    testpaper_id bigint                 NOT NULL
);


ALTER TABLE public.exam
    OWNER TO postgres;

--
-- Name: exam_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exam_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exam_id_seq
    OWNER TO postgres;

--
-- Name: exam_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exam_id_seq OWNED BY public.exam.id;


--
-- Name: exercise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exercise
(
    id             bigint                  NOT NULL,
    description    character varying(4096) NOT NULL,
    expecteddata   character varying(2048) NOT NULL,
    expectedsql    character varying(2048),
    inputdata      character varying(2048),
    inputsql       character varying(2048),
    score          integer                 NOT NULL,
    title          character varying(512)  NOT NULL,
    teacher_number bigint                  NOT NULL,
    type           integer,
    column_order   boolean DEFAULT true    NOT NULL,
    row_order      boolean DEFAULT true    NOT NULL
);


ALTER TABLE public.exercise
    OWNER TO postgres;

--
-- Name: exercise_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exercise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exercise_id_seq
    OWNER TO postgres;

--
-- Name: exercise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exercise_id_seq OWNED BY public.exercise.id;


--
-- Name: exerciseconfig; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exerciseconfig
(
    id       bigint NOT NULL,
    exercise bigint,
    score    integer
);


ALTER TABLE public.exerciseconfig
    OWNER TO postgres;

--
-- Name: exerciseconfig_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exerciseconfig_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exerciseconfig_id_seq
    OWNER TO postgres;

--
-- Name: exerciseconfig_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exerciseconfig_id_seq OWNED BY public.exerciseconfig.id;


--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student
(
    number   bigint                NOT NULL,
    name     character varying(16) NOT NULL,
    password character varying(60) NOT NULL,
    clazz_id bigint                NOT NULL
);


ALTER TABLE public.student
    OWNER TO postgres;

--
-- Name: student_number_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_number_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.student_number_seq
    OWNER TO postgres;

--
-- Name: student_number_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.student_number_seq OWNED BY public.student.number;


--
-- Name: teacher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teacher
(
    number   bigint                NOT NULL,
    name     character varying(16) NOT NULL,
    password character varying(60) NOT NULL
);


ALTER TABLE public.teacher
    OWNER TO postgres;

--
-- Name: teacher_number_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.teacher_number_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teacher_number_seq
    OWNER TO postgres;

--
-- Name: teacher_number_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.teacher_number_seq OWNED BY public.teacher.number;


--
-- Name: teaching; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teaching
(
    id             bigint NOT NULL,
    clazz_id       bigint NOT NULL,
    teacher_number bigint NOT NULL
);


ALTER TABLE public.teaching
    OWNER TO postgres;

--
-- Name: teaching_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.teaching_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teaching_id_seq
    OWNER TO postgres;

--
-- Name: teaching_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.teaching_id_seq OWNED BY public.teaching.id;


--
-- Name: testpaper; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.testpaper
(
    id              bigint                 NOT NULL,
    exerciseconfigs bigint[],
    title           character varying(512) NOT NULL,
    teacher_number  bigint                 NOT NULL,
    score           integer DEFAULT 0      NOT NULL
);


ALTER TABLE public.testpaper
    OWNER TO postgres;

--
-- Name: testpaper_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.testpaper_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.testpaper_id_seq
    OWNER TO postgres;

--
-- Name: testpaper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.testpaper_id_seq OWNED BY public.testpaper.id;


--
-- Name: answer id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer
    ALTER COLUMN id SET DEFAULT nextval('public.answer_id_seq'::regclass);


--
-- Name: answersheet id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answersheet
    ALTER COLUMN id SET DEFAULT nextval('public.answersheet_id_seq'::regclass);


--
-- Name: clazz id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clazz
    ALTER COLUMN id SET DEFAULT nextval('public.clazz_id_seq'::regclass);


--
-- Name: exam id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam
    ALTER COLUMN id SET DEFAULT nextval('public.exam_id_seq'::regclass);


--
-- Name: exercise id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ALTER COLUMN id SET DEFAULT nextval('public.exercise_id_seq'::regclass);


--
-- Name: exerciseconfig id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exerciseconfig
    ALTER COLUMN id SET DEFAULT nextval('public.exerciseconfig_id_seq'::regclass);


--
-- Name: student number; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ALTER COLUMN number SET DEFAULT nextval('public.student_number_seq'::regclass);


--
-- Name: teacher number; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teacher
    ALTER COLUMN number SET DEFAULT nextval('public.teacher_number_seq'::regclass);


--
-- Name: teaching id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ALTER COLUMN id SET DEFAULT nextval('public.teaching_id_seq'::regclass);


--
-- Name: testpaper id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.testpaper
    ALTER COLUMN id SET DEFAULT nextval('public.testpaper_id_seq'::regclass);


--
-- Name: answer answer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer
    ADD CONSTRAINT answer_pkey PRIMARY KEY (id);


--
-- Name: answersheet answersheet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answersheet
    ADD CONSTRAINT answersheet_pkey PRIMARY KEY (id);


--
-- Name: clazz clazz_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clazz
    ADD CONSTRAINT clazz_pkey PRIMARY KEY (id);


--
-- Name: exam exam_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam
    ADD CONSTRAINT exam_pkey PRIMARY KEY (id);


--
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);


--
-- Name: exerciseconfig exerciseconfig_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exerciseconfig
    ADD CONSTRAINT exerciseconfig_pkey PRIMARY KEY (id);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (number);


--
-- Name: teacher teacher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teacher
    ADD CONSTRAINT teacher_pkey PRIMARY KEY (number);


--
-- Name: teaching teaching_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT teaching_pkey PRIMARY KEY (id);


--
-- Name: testpaper testpaper_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.testpaper
    ADD CONSTRAINT testpaper_pkey PRIMARY KEY (id);


--
-- Name: answersheet uk3xsm8wkr8ebsjbyikccitirey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answersheet
    ADD CONSTRAINT uk3xsm8wkr8ebsjbyikccitirey UNIQUE (exam_id, student_number);


--
-- Name: answer uk8w4u45v6rkjvjbiux4v6jwmuf; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer
    ADD CONSTRAINT uk8w4u45v6rkjvjbiux4v6jwmuf UNIQUE (answersheet_id, exercise_config_id);


--
-- Name: clazz uk9su9ka0w2dleikswltxlxoy4t; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clazz
    ADD CONSTRAINT uk9su9ka0w2dleikswltxlxoy4t UNIQUE (grade, name);


--
-- Name: teaching ukkbo5pdh5w95nx9d8rbk14756a; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT ukkbo5pdh5w95nx9d8rbk14756a UNIQUE (teacher_number, clazz_id);


--
-- Name: teaching ukkp9hax7t2m24pyq1spsk49kdc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT ukkp9hax7t2m24pyq1spsk49kdc UNIQUE (teacher_number, clazz_id);


--
-- Name: clazz ukmaxpoyvy95k3e27ac12fj5v0t; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clazz
    ADD CONSTRAINT ukmaxpoyvy95k3e27ac12fj5v0t UNIQUE (grade, name);


--
-- Name: idx3up80j396y3ioi39u367gy3bn; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx3up80j396y3ioi39u367gy3bn ON public.student USING btree (name);


--
-- Name: idx7pb8owoegbhhcrpopw4o1ykcr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx7pb8owoegbhhcrpopw4o1ykcr ON public.student USING btree (name);


--
-- Name: idxgofyeb3384qck4viavkb7s47i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxgofyeb3384qck4viavkb7s47i ON public.teacher USING btree (name);


--
-- Name: answer answer_exerciseconfig_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer
    ADD CONSTRAINT answer_exerciseconfig_id_fk FOREIGN KEY (exercise_config_id) REFERENCES public.exerciseconfig (id);


--
-- Name: exerciseconfig exerciseconfig_exercise_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exerciseconfig
    ADD CONSTRAINT exerciseconfig_exercise_id_fk FOREIGN KEY (exercise) REFERENCES public.exercise (id);


--
-- Name: exam fk2e64poixh3mxwtrpns54q4355; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam
    ADD CONSTRAINT fk2e64poixh3mxwtrpns54q4355 FOREIGN KEY (teaching_id) REFERENCES public.teaching (id);


--
-- Name: exercise fk2jv7jehbsm1y9an3pudscandp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT fk2jv7jehbsm1y9an3pudscandp FOREIGN KEY (teacher_number) REFERENCES public.teacher (number);


--
-- Name: teaching fk5t9hjd8q681p7dx2jsa2gb2hv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT fk5t9hjd8q681p7dx2jsa2gb2hv FOREIGN KEY (teacher_number) REFERENCES public.teacher (number);


--
-- Name: teaching fk8dydn3e2bwimy5h82y18kx418; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT fk8dydn3e2bwimy5h82y18kx418 FOREIGN KEY (teacher_number) REFERENCES public.teacher (number);


--
-- Name: exam fkedm4x6vgjs4otxwt84y4pp3rt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam
    ADD CONSTRAINT fkedm4x6vgjs4otxwt84y4pp3rt FOREIGN KEY (testpaper_id) REFERENCES public.testpaper (id);


--
-- Name: teaching fklntg5nqgjkjuj3gbcydrut739; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching
    ADD CONSTRAINT fklntg5nqgjkjuj3gbcydrut739 FOREIGN KEY (clazz_id) REFERENCES public.clazz (id);


--
-- Name: student fkr6vhwx3i4blsfj07c5ttqwj9p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT fkr6vhwx3i4blsfj07c5ttqwj9p FOREIGN KEY (clazz_id) REFERENCES public.clazz (id);


--
-- Name: testpaper fks5oxteg6nqy4ha2cswiyk0njt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.testpaper
    ADD CONSTRAINT fks5oxteg6nqy4ha2cswiyk0njt FOREIGN KEY (teacher_number) REFERENCES public.teacher (number);


--
-- Name: student fkt382048dvcet1ce33g12qvx70; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT fkt382048dvcet1ce33g12qvx70 FOREIGN KEY (clazz_id) REFERENCES public.clazz (id);


--
-- PostgreSQL database dump complete
--

