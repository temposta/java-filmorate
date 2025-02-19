MERGE INTO MPARATINGS (mpa_id, name, description) VALUES (1, 'G    ', 'у фильма нет возрастных ограничений');
MERGE INTO MPARATINGS (mpa_id, name, description) VALUES (2, 'PG   ', 'детям рекомендуется смотреть фильм с родителями');
MERGE INTO MPARATINGS (mpa_id, name, description) VALUES (3, 'PG-13', 'детям до 13 лет просмотр не желателен');
MERGE INTO MPARATINGS (mpa_id, name, description) VALUES (4, 'R    ', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
MERGE INTO MPARATINGS (mpa_id, name, description) VALUES (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');

MERGE INTO GENRES (genre_id, name) VALUES (1, 'Комедия');
MERGE INTO GENRES (genre_id, name) VALUES (2, 'Драма');
MERGE INTO GENRES (genre_id, name) VALUES (3, 'Мультфильм');
MERGE INTO GENRES (genre_id, name) VALUES (4, 'Триллер');
MERGE INTO GENRES (genre_id, name) VALUES (5, 'Документальный');
MERGE INTO GENRES (genre_id, name) VALUES (6, 'Боевик');

delete
from users;
insert into users (user_id, email, login, name, birth_date)
values (2, 'test1@test.ru', 'test1', 'test1', '2010-11-7');
insert into users (user_id, email, login, name, birth_date)
values (3, 'test2@test.ru', 'test2', 'test2', '2010-11-8');
insert into users (user_id, email, login, name, birth_date)
values (4, 'test3@test.ru', 'test3', 'test3', '2010-11-9');
insert into users (user_id, email, login, name, birth_date)
values (5, 'test4@test.ru', 'test4', 'test4', '2010-11-11');
insert into users (user_id, email, login, name, birth_date)
values (6, 'test5@test.ru', 'test5', 'test5', '2010-11-13');
insert into users (user_id, email, login, name, birth_date)
values (7, 'test6@test.ru', 'test6', 'test6', '2010-11-15');
delete
from films;
insert into films (film_id, name, description, release_date, duration, mpa_id)
values (2,'film1', 'description1', '2000-01-10', 120, 1);
insert into films (film_id,name, description, release_date, duration, mpa_id)
values (3,'film2', 'description2', '2000-01-11', 130, 2);
insert into films (film_id,name, description, release_date, duration, mpa_id)
values (4,'film3', 'description3', '2000-01-12', 110, 3);
insert into films (film_id,name, description, release_date, duration, mpa_id)
values (5,'film4', 'description4', '2000-01-13', 100, 4);
insert into films (film_id,name, description, release_date, duration, mpa_id)
values (6,'film5', 'description5', '2000-01-14', 150, 5);

insert into likes (film_id, user_id) values (2,2);
insert into likes (film_id, user_id) values (2,3);
insert into likes (film_id, user_id) values (2,4);
insert into likes (film_id, user_id) values (2,5);
insert into likes (film_id, user_id) values (2,6);
insert into likes (film_id, user_id) values (2,7);
insert into likes (film_id, user_id) values (3,5);
insert into likes (film_id, user_id) values (3,6);
insert into likes (film_id, user_id) values (3,7);
insert into likes (film_id, user_id) values (4,2);
insert into likes (film_id, user_id) values (4,4);
insert into likes (film_id, user_id) values (5,2);
insert into likes (film_id, user_id) values (5,3);
insert into likes (film_id, user_id) values (5,7);
insert into likes (film_id, user_id) values (6,2);

