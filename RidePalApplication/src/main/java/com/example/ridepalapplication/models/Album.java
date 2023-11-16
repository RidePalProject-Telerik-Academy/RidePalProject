package com.example.ridepalapplication.models;

import jakarta.persistence.*;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "album_track_list_url")
    private String albumTrackListUrl;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Album() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumTrackListUrl() {
        return albumTrackListUrl;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumTrackListUrl(String albumTrackListUrl) {
        this.albumTrackListUrl = albumTrackListUrl;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Album album = (Album) obj;
        return id == album.id;
    }
}
