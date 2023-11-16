create schema ride_pal;

create table albums
(
    id   int auto_increment
        primary key,
    name varchar(255) not null
);

create table artists
(
    id   int auto_increment
        primary key,
    name varchar(100) not null
);

create table genres
(
    id   int auto_increment
        primary key,
    name varchar(100) not null
);

create table songs
(
    id          int auto_increment
        primary key,
    title       varchar(100)  not null,
    duration    int           not null,
    `rank`      int           not null,
    preview_url varchar(2000) not null,
    artist_id   int           not null,
    album_id    int           not null,
    genre       int           not null,
    constraint songs_albums_id_fk
        foreign key (album_id) references albums (id),
    constraint songs_artists_id_fk
        foreign key (artist_id) references artists (id),
    constraint songs_genres_id_fk
        foreign key (genre) references genres (id)
);

create table tags
(
    id   int auto_increment
        primary key,
    name varchar(100) not null
);

create table users
(
    id         int auto_increment
        primary key,
    username   varchar(15)  not null,
    password   varchar(128) not null,
    email      varchar(320) not null,
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null,
    is_admin   tinyint(1)   not null
);

create table playlists
(
    id      int auto_increment
        primary key,
    title   varchar(100) not null,
    user_id int          not null,
    constraint playlists_users_id_fk
        foreign key (user_id) references users (id)
);

create table playlists_songs
(
    song_id     int not null,
    playlist_id int not null,
    constraint playlists_songs_playlists_id_fk
        foreign key (playlist_id) references playlists (id),
    constraint playlists_songs_songs_id_fk
        foreign key (song_id) references songs (id)
);

create table playlists_tags
(
    playlist_id int not null,
    tag_id      int not null,
    constraint playlists_tags_playlists_id_fk
        foreign key (playlist_id) references playlists (id),
    constraint playlists_tags_tags_id_fk
        foreign key (tag_id) references tags (id)
);

