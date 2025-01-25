CREATE TABLE IF NOT EXISTS genre
(
    genre_id serial            NOT NULL,
    name     character varying NOT NULL,
    PRIMARY KEY (genre_id)
);

COMMENT ON TABLE genre
    IS 'Таблица жанров фильмов';

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  integer NOT NULL,
    genre_id integer NOT NULL,
    CONSTRAINT common_constraint UNIQUE (film_id, genre_id)
        INCLUDE (film_id, genre_id)
);

COMMENT ON TABLE film_genre
    IS 'Перечень жанров, относящихся к конкретному фильму';

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id      smallserial  NOT NULL,
    name        character(5) NOT NULL,
    description character varying,
    PRIMARY KEY (mpa_id)
);

COMMENT ON TABLE mpa
    IS 'Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)';

CREATE TABLE IF NOT EXISTS movie
(
    film_id      serial            NOT NULL,
    name         character varying NOT NULL,
    description  character varying(200),
    release_date date,
    duration     integer,
    mpa_id       integer,
    PRIMARY KEY (film_id)
);

COMMENT ON TABLE movie
    IS 'Фильмы';

CREATE TABLE IF NOT EXISTS likes
(
    film_id integer NOT NULL,
    user_id integer NOT NULL,
    CONSTRAINT common UNIQUE (film_id, user_id)
        INCLUDE (film_id, user_id)
);

ALTER TABLE IF EXISTS film_genre
    ADD CONSTRAINT genre_id FOREIGN KEY (genre_id)
        REFERENCES genre (genre_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE IF EXISTS film_genre
    ADD CONSTRAINT film FOREIGN KEY (film_id)
        REFERENCES movie (film_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID;


ALTER TABLE IF EXISTS movie
    ADD CONSTRAINT mpa FOREIGN KEY (mpa_id)
        REFERENCES mpa (mpa_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE IF EXISTS likes
    ADD CONSTRAINT film FOREIGN KEY (film_id)
        REFERENCES movie (film_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;
