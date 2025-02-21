create table if not exists GENRES
(
    GENRE_ID INTEGER           not null primary key,
    NAME     CHARACTER VARYING not null
);

comment on table GENRES is 'Таблица жанров фильмов';

create table if not exists MPARATINGS
(
    MPA_ID      INTEGER      not null
        primary key,
    NAME        CHARACTER(5) not null,
    DESCRIPTION CHARACTER VARYING
);

comment on table MPARATINGS is 'Рейтинг Ассоциации кинокомпаний ' ||
                               '(англ. Motion Picture Association, сокращённо МРА)';

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment primary key,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    FOREIGN KEY (MPA_ID) REFERENCES MPARATINGS (MPA_ID)
);

comment on table FILMS is 'Фильмы';

create table if not exists FILMGENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    primary key (FILM_ID, GENRE_ID),
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRES (GENRE_ID)
        ON UPDATE CASCADE
);

comment on table FILMGENRES is 'Перечень жанров, относящихся к конкретному фильму';

create table if not exists USERS
(
    USER_ID    INTEGER auto_increment primary key,
    EMAIL      CHARACTER VARYING not null unique,
    LOGIN      CHARACTER VARYING,
    NAME       CHARACTER VARYING,
    BIRTH_DATE DATE
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    primary key (FILM_ID, USER_ID),
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (FILM_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

create table if not exists FRIENDS
(
    USER_ID   INTEGER NOT NULL,
    FRIEND_ID INTEGER NOT NULL,
    PRIMARY KEY (USER_ID, FRIEND_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (FRIEND_ID) REFERENCES USERS (USER_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
