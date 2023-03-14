create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER NOT NULL,
    MPA_NAME CHARACTER VARYING(100) NOT NULL,
    constraint MPA_ID
        primary key (MPA_ID)
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER AUTO_INCREMENT,
    GENRE_NAME CHARACTER VARYING(100) NOT NULL,
    constraint GENRES
        primary key (GENRE_ID)
);

create table IF NOT EXISTS USERS
(
    USER_ID   LONG AUTO_INCREMENT,
    USER_NAME CHARACTER VARYING(100),
    EMAIL     CHARACTER VARYING      NOT NULL,
    LOGIN     CHARACTER VARYING(100) NOT NULL,
    BIRTHDAY  DATE,
    constraint USER_ID
        primary key (USER_ID)
);

create unique index IF NOT EXISTS USERS_EMAIL_UINDEX
    on USERS (EMAIL);

create table IF NOT EXISTS FILMS
(
    FILM_ID       LONG AUTO_INCREMENT,
    FILM_NAME     CHARACTER VARYING(200) NOT NULL,
    DESCRIPTION   CHARACTER VARYING(200),
    RELEASE_DATE  DATE,
    DURATION      INTEGER,
    RATE          INTEGER,
    MPA_ID        INTEGER,
    constraint FILMS
        primary key (FILM_ID),
    constraint "FK_MPA_ID_MPA"
        foreign key (MPA_ID) references MPA
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID   LONG PRIMARY KEY AUTO_INCREMENT,
    DIRECTOR_NAME VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS FILM_DIRECTORS
(
    FILM_ID     LONG REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    DIRECTOR_ID LONG REFERENCES DIRECTORS (DIRECTOR_ID) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  LONG NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    constraint FILM_GENRES
        primary key (FILM_ID, GENRE_ID),
    constraint FK_FILM_FILMS
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FK_GENRE_GENRES
        foreign key (GENRE_ID) references GENRES ON DELETE CASCADE
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID LONG NOT NULL,
    USER_ID LONG NOT NULL,
    constraint FILM_LIKES
        primary key (FILM_ID, USER_ID),
    constraint FK_FILM_ID_FILMS
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FK_FILM_LIKES_USERS
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

create table IF NOT EXISTS USER_FRIENDS
(
    USER_ID   LONG NOT NULL,
    FRIEND_ID LONG NOT NULL,
    constraint USER_FRIENDS
        primary key (USER_ID, FRIEND_ID),
    constraint FK_FRIEND_ID_USERS
        foreign key (FRIEND_ID) references USERS ON DELETE CASCADE,
    constraint FK_USER_ID_USERS
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FILM_DIRECTORS
(
    film_id     LONG REFERENCES films (film_id) ON DELETE CASCADE,
    director_id LONG REFERENCES directors (director_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id   LONG PRIMARY KEY AUTO_INCREMENT,
    content     VARCHAR,
    useful      INTEGER,
    is_positive BOOLEAN NOT NULL,
    user_id     LONG REFERENCES users (user_id) ON DELETE CASCADE,
    film_id     LONG REFERENCES films (film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_reactions
(
    user_id       LONG REFERENCES users (user_id),
    review_id     LONG REFERENCES reviews (review_id) ON DELETE CASCADE,
    reaction_type VARCHAR NOT NULL,
    PRIMARY KEY (review_id, user_id)
);

