create table IF NOT EXISTS MPA
(
    MPA_ID  INTEGER NOT NULL,
    MPA_NAME CHARACTER VARYING(100) not null,
    constraint MPA_ID
        primary key (MPA_ID)
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(100) not null,
    constraint GENRES
        primary key (GENRE_ID)
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    USER_NAME CHARACTER VARYING(100),
    EMAIL     CHARACTER VARYING      not null,
    LOGIN     CHARACTER VARYING(100) not null,
    BIRTHDAY  DATE,
    constraint USER_ID
        primary key (USER_ID)
);

create unique index IF NOT EXISTS USERS_EMAIL_UINDEX
    on USERS (EMAIL);

create table IF NOT EXISTS FILMS
(
    FILM_ID       INTEGER auto_increment,
    FILM_NAME     CHARACTER VARYING(200) not null,
    DESCRIPTION   CHARACTER VARYING(200),
    RELEASE_DATE  DATE,
    DURATION      INTEGER,
    RATE          INTEGER,
    MPA_ID INTEGER,
    constraint FILMS
        primary key (FILM_ID),
    constraint "FK_MPA_ID_MPA"
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES
        primary key (FILM_ID, GENRE_ID),
    constraint FK_FILM_FILMS
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FK_GENRE_GENRES
        foreign key (GENRE_ID) references GENRES ON DELETE CASCADE
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKES
        primary key (FILM_ID, USER_ID),
    constraint FK_FILM_ID_FILMS
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FK_FILM_LIKES_USERS
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

create table IF NOT EXISTS USER_FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint USER_FRIENDS
        primary key (USER_ID, FRIEND_ID),
    constraint FK_FRIEND_ID_USERS
        foreign key (FRIEND_ID) references USERS ON DELETE CASCADE,
    constraint FK_USER_ID_USERS
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

