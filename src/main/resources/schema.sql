CREATE TABLE IF NOT EXISTS genre
(
    genre_id integer         NOT NULL,
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
);

COMMENT ON TABLE film_genre
    IS 'Перечень жанров, относящихся к конкретному фильму';

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id      integer  NOT NULL,
    name        character(5) NOT NULL,
    description character varying,
    PRIMARY KEY (mpa_id)
);

COMMENT ON TABLE mpa
    IS 'Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)';

CREATE TABLE IF NOT EXISTS movie
(
    film_id      integer            NOT NULL,
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
);
