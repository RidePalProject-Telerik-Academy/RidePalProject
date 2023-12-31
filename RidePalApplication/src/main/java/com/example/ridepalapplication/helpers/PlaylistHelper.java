package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.GenreRepository;


import java.util.List;
import java.util.Set;

public class PlaylistHelper {

    public static Genre extractGenres(GenreRepository genreRepository, GenreDto genreDto) {
        Genre genre = genreRepository.findByName(genreDto.getName());

        if (genre == null) {
            throw new EntityNotFoundException("Genre", "name", genreDto.getName());
        }
        return genre;
    }

    public static void updatePlaylistDetails(Playlist playlistToUpdate, Song song, String action) {
        int temp = Math.toIntExact(song.getDuration());
        long totalPlaylistRank = playlistToUpdate.getSongs().size() * playlistToUpdate.getRank();

        if (action.equals("add")) {
            totalPlaylistRank += song.getRank();
            playlistToUpdate.addSong(song);
            playlistToUpdate.setDuration(playlistToUpdate.getDuration() + temp);
        } else {
            if (playlistToUpdate.getSongs().size() == 1) {
                playlistToUpdate.getSongs().remove(song);
                playlistToUpdate.setDuration(0);
                playlistToUpdate.setRank(0L);
                return;
            } else {
                totalPlaylistRank -= song.getRank();
                playlistToUpdate.removeSong(song);
                playlistToUpdate.setDuration(playlistToUpdate.getDuration() - temp);
            }
        }
        playlistToUpdate.setRank(Math.absExact(totalPlaylistRank) / playlistToUpdate.getSongs().size());
    }

    public static void updatePlaylistDetails(Playlist playlist, Long totalRank, Set<Song> playlistSongs, int totalPlaylistDuration, Set<Genre> genres) {
        playlist.setRank(totalRank / playlistSongs.size());
        playlist.setDuration(totalPlaylistDuration);
        playlist.setSongs(playlistSongs);
        playlist.setGenres(genres);
    }

    public static List<GenreDto> verifyTotalGenrePercentage(List<GenreDto> genreDtoList) {
        int totalGenrePercentage = 0;
        for (GenreDto genreDto : genreDtoList) {
            totalGenrePercentage += genreDto.getPercentage();

        }
        if (totalGenrePercentage != 100) {
            throw new IllegalArgumentException("Invalid genres percentage!");
        }
        return genreDtoList;
    }

    public static void getSongArtist(List<Song> songs, List<Long> artistsId) {
        Long artistId = songs.get(0).getArtist().getId();
        artistsId.add(artistId);
    }

    public static void validateGenreAvailability(List<Song> songs, Genre genre) {
        if (songs.isEmpty()) {
            throw new UnsupportedOperationException(String.format("The application does not support enough unique songs with genre " +
                    "%s to satisfy your request !", genre.getName()));
        }
    }
    public static boolean isPercentageZero(GenreDto genreDto) {
        return genreDto.getPercentage() == 0;
    }
}
