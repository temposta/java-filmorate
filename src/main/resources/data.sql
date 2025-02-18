insert into RATING (name)
select 'G' from dual
where not exists (select 1 from RATING where name = 'G');

insert into RATING (name)
select 'PG' from dual
where not exists (select 1 from RATING where name = 'PG');

insert into RATING (name)
select 'PG-13' from dual
where not exists (select 1 from RATING where name = 'PG-13');

insert into RATING (name)
select 'R' from dual
where not exists (select 1 from RATING where name = 'R');

insert into RATING (name)
select 'NC-17' from dual
where not exists (select 1 from RATING where name = 'NC-17');

insert into GENRE (name)
select 'Комедия' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'комедия');

insert into GENRE (name)
select 'Драма' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'драма');

insert into GENRE (name)
select 'Мультфильм' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'мультфильм');

insert into GENRE (name)
select 'Триллер' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'триллер');

insert into GENRE (name)
select 'Документальный' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'документальный');

insert into GENRE (name)
select 'Боевик' from dual
where not exists (select 1 from GENRE where LOWER(name) = 'боевик');