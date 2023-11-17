package com.example.ridepalapplication.models;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "link")
    private String link;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "rank")
    private Long rank;
    @Column(name = "preview_url")
    private String previewUrl;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Song() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getRank() {
        return rank;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Song song = (Song) obj;
        return id == song.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
