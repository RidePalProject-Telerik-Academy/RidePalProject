create schema ride_pal;

create or replace table ride_pal.artists
(
    id                    bigint auto_increment
        primary key,
    artist_track_list_url varchar(255) null,
    name                  varchar(255) null,
    constraint artists_pk
        unique (artist_track_list_url),
    constraint artists_pk2
        unique (name)
);
create or replace table ride_pal.genres
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);
create or replace table ride_pal.albums
(
    genre_id             bigint       null,
    id                   bigint auto_increment
        primary key,
    album_track_list_url varchar(255) null,
    name                 varchar(255) null,
    constraint FKgo1exs517g8n9osc20m3qidib
        foreign key (genre_id) references ride_pal.genres (id)
);
create or replace table ride_pal.roles
(
    role_id   bigint       not null
        primary key,
    authority varchar(255) null
);
create or replace table ride_pal.roles_seq
(
    next_not_cached_value bigint(21)          not null,
    minimum_value         bigint(21)          not null,
    maximum_value         bigint(21)          not null,
    start_value           bigint(21)          not null comment 'start value when sequences is created or value if RESTART is used',
    increment             bigint(21)          not null comment 'increment value',
    cache_size            bigint(21) unsigned not null,
    cycle_option          tinyint(1) unsigned not null comment '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
    cycle_count           bigint(21)          not null comment 'How many cycles have been done'
);
create or replace table ride_pal.songs
(
    duration    bigint       null,
    `rank`      bigint       null,
    album_id    bigint       null,
    artist_id   bigint       null,
    id          bigint auto_increment
        primary key,
    link        varchar(255) null,
    preview_url varchar(255) null,
    title       varchar(255) null,
    constraint FKdjq2ujqovw5rc14q60f8p6b6e
        foreign key (artist_id) references ride_pal.artists (id),
    constraint FKte4gkb2cqtk2erfa87oopj2cj
        foreign key (album_id) references ride_pal.albums (id)
);
create or replace table ride_pal.synchronizations
(
    id        bigint auto_increment
        primary key,
    status    varchar(255) null,
    sync_time datetime(6)  null
);
create or replace table ride_pal.tags
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);
create or replace table ride_pal.users
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null
);
create or replace table ride_pal.playlists
(
    id       bigint auto_increment
        primary key,
    user_id  bigint           null,
    name     varchar(255)     null,
    duration int    default 0 not null,
    `rank`   bigint default 0 not null,
    constraint FKtgjwvfg23v990xk7k0idmqbrj
        foreign key (user_id) references ride_pal.users (id)
);
create or replace table ride_pal.playlist_songs
(
    playlist_id bigint not null,
    song_id     bigint not null,
    primary key (playlist_id, song_id),
    constraint FK5xu79gpgpc1p4tku7j6dv2skb
        foreign key (song_id) references ride_pal.songs (id),
    constraint FKqfutupgj870d2k31ldxqqwr8w
        foreign key (playlist_id) references ride_pal.playlists (id)
);
create or replace table ride_pal.playlist_tags
(
    playlist_id bigint not null,
    tag_id      bigint not null,
    primary key (playlist_id, tag_id),
    constraint FKkodb4iguq6wkc54ka2ka7a8wt
        foreign key (playlist_id) references ride_pal.playlists (id),
    constraint FKn0ps40ntd250eq7xo1017me29
        foreign key (tag_id) references ride_pal.tags (id)
);
create or replace table ride_pal.playlists_genres
(
    playlist_id bigint not null,
    genre_id    bigint not null,
    primary key (playlist_id, genre_id),
    constraint FK3a8yagcmit8el6tvdritos152
        foreign key (playlist_id) references ride_pal.playlists (id),
    constraint FKag10aranng7n5hocw20axqokj
        foreign key (genre_id) references ride_pal.genres (id)
);
create or replace table ride_pal.users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references ride_pal.users (id),
    constraint FKj6m8fwv7oqv74fcehir1a9ffy
        foreign key (role_id) references ride_pal.roles (role_id)
);

[10:11] Boris Neykov
albums.sql
[10:24] Simona Nedeva
Archive.zip
[10:52] Simona Nedeva
create or replace table ride_pal.artists
(
    id                    bigint auto_increment
        primary key,
    artist_track_list_url varchar(255) null,
    name                  varchar(255) null,
    constraint artists_pk
        unique (artist_track_list_url),
    constraint artists_pk2
        unique (name)
);
create or replace table ride_pal.genres
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);
create or replace table ride_pal.albums
(
    genre_id             bigint       null,
    id                   bigint auto_increment
        primary key,
    album_track_list_url varchar(255) null,
    name                 varchar(255) null,
    constraint FKgo1exs517g8n9osc20m3qidib
        foreign key (genre_id) references ride_pal.genres (id)
);
create or replace table ride_pal.roles
(
    role_id   bigint       not null
        primary key,
    authority varchar(255) null
);
create or replace table ride_pal.roles_seq
(
    next_not_cached_value bigint(21)          not null,
    minimum_value         bigint(21)          not null,
    maximum_value         bigint(21)          not null,
    start_value           bigint(21)          not null comment 'start value when sequences is created or value if RESTART is used',
    increment             bigint(21)          not null comment 'increment value',
    cache_size            bigint(21) unsigned not null,
    cycle_option          tinyint(1) unsigned not null comment '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
    cycle_count           bigint(21)          not null comment 'How many cycles have been done'
);
create or replace table ride_pal.songs
(
    duration    bigint       null,
    `rank`      bigint       null,
    album_id    bigint       null,
    artist_id   bigint       null,
    id          bigint auto_increment
        primary key,
    link        varchar(255) null,
    preview_url varchar(255) null,
    title       varchar(255) null,
    constraint FKdjq2ujqovw5rc14q60f8p6b6e
        foreign key (artist_id) references ride_pal.artists (id),
    constraint FKte4gkb2cqtk2erfa87oopj2cj
        foreign key (album_id) references ride_pal.albums (id)
);
create or replace table ride_pal.synchronizations
(
    id        bigint auto_increment
        primary key,
    status    varchar(255) null,
    sync_time datetime(6)  null
);
create or replace table ride_pal.tags
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);
create or replace table ride_pal.users
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null
);
create or replace table ride_pal.playlists
(
    id       bigint auto_increment
        primary key,
    user_id  bigint           null,
    name     varchar(255)     null,
    duration int    default 0 not null,
    `rank`   bigint default 0 not null,
    constraint FKtgjwvfg23v990xk7k0idmqbrj
        foreign key (user_id) references ride_pal.users (id)
);
create or replace table ride_pal.playlist_songs
(
    playlist_id bigint not null,
    song_id     bigint not null,
    primary key (playlist_id, song_id),
    constraint FK5xu79gpgpc1p4tku7j6dv2skb
        foreign key (song_id) references ride_pal.songs (id),
    constraint FKqfutupgj870d2k31ldxqqwr8w
        foreign key (playlist_id) references ride_pal.playlists (id)
);
create or replace table ride_pal.playlist_tags
(
    playlist_id bigint not null,
    tag_id      bigint not null,
    primary key (playlist_id, tag_id),
    constraint FKkodb4iguq6wkc54ka2ka7a8wt
        foreign key (playlist_id) references ride_pal.playlists (id),
    constraint FKn0ps40ntd250eq7xo1017me29
        foreign key (tag_id) references ride_pal.tags (id)
);
create or replace table ride_pal.playlists_genres
(
    playlist_id bigint not null,
    genre_id    bigint not null,
    primary key (playlist_id, genre_id),
    constraint FK3a8yagcmit8el6tvdritos152
        foreign key (playlist_id) references ride_pal.playlists (id),
    constraint FKag10aranng7n5hocw20axqokj
        foreign key (genre_id) references ride_pal.genres (id)
);
create or replace table ride_pal.users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references ride_pal.users (id),
    constraint FKj6m8fwv7oqv74fcehir1a9ffy
        foreign key (role_id) references ride_pal.roles (role_id)
);
