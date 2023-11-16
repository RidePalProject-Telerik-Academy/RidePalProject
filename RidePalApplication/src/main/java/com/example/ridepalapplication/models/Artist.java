package com.example.ridepalapplication.models;

import jakarta.persistence.*;

@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "artist_track_list_url")
    private String artistTrackListUrl;

    public Artist() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtistTrackListUrl() {
        return artistTrackListUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistTrackListUrl(String artistTrackListUrl) {
        this.artistTrackListUrl = artistTrackListUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
