# java-filmorate
Template repository for Filmorate project.

Схема базы данных https://github.com/I-Wish-I-Knew/java-filmorate/blob/add_database/databaseFilmorate.png

Примеры запросов SQL:

- для получения списка 10 самых популярных фильмов:
  SELECT film_id
  FROM film_likes
  ORDER BY rate DESC
  LIMIT 10;

- для получения списка друзей пользователя
  SELECT friend_id
  FROM user_friends
  WHERE user_id=(желаемый идентификатор);

- для получения списка жанров для фильма
  SELECT g.genre_name
  FROM genres AS g
  INNER JOIN film_genres AS fg ON genres fg.genre_id=g.genre_id
  WHERE film_id=(желаемый идентификатор);