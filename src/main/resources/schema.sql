CREATE TABLE IF NOT EXISTS film (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(100),
  description varchar(255),
  release_date date,
  duration integer,
  rating bigint
);

CREATE TABLE IF NOT EXISTS "user" (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar(255),
  login varchar(50),
  name varchar(100),
  birthday date
);

CREATE TABLE IF NOT EXISTS "like" (
  user_id bigint,
  film_id bigint,
  PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS friendship (
  user_id bigint,
  friend_id bigint,
  is_confirmed bool,
  PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS genre (
  id bigint GENERATED BY DEFAULT AS IDENTITY  PRIMARY KEY,
  name varchar(50)
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id bigint,
  genre_id bigint,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS rating (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(50)
);

ALTER TABLE "like" ADD FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE NO ACTION;

ALTER TABLE "like" ADD FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE NO ACTION;

ALTER TABLE friendship ADD FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE NO ACTION;

ALTER TABLE friendship ADD FOREIGN KEY (friend_id) REFERENCES "user" (id) ON DELETE NO ACTION;

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE NO ACTION;

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE NO ACTION;

ALTER TABLE film ADD FOREIGN KEY (rating) REFERENCES rating (id) ON DELETE CASCADE;