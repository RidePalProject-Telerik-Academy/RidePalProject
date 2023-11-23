package com.example.ridepalapplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "duration")
    private int duration;
    @Column(name = "rank")
    private Long rank;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> songs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_tags",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Playlist() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public Long getRank() {
        return rank;
    }

    public User getCreator() {
        return creator;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    public void setTags(Tag tag) {
        tags.add(tag);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return id == playlist.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
