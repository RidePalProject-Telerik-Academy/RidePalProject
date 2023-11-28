package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.GenreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    public  static void updatePlaylistDetails(Playlist playlistToUpdate, Song song, String action) {
        int temp = Math.toIntExact(song.getDuration());
        long totalPlaylistRank = playlistToUpdate.getSongs().size() * playlistToUpdate.getRank();

        if (action.equals("add")) {
            totalPlaylistRank += song.getRank();
            playlistToUpdate.addSong(song);
            playlistToUpdate.setDuration(playlistToUpdate.getDuration() + temp);
        } else {
            totalPlaylistRank -= song.getRank();
            playlistToUpdate.removeSong(song);
            playlistToUpdate.setDuration(playlistToUpdate.getDuration() - temp);
        }
        playlistToUpdate.setRank(totalPlaylistRank / playlistToUpdate.getSongs().size());

    }
    public static void updatePlaylistDetails(Playlist playlist, Long totalRank, Set<Song> playlistSongs, int totalPlaylistDuration) {
        playlist.setRank(totalRank / playlistSongs.size());
        playlist.setDuration(totalPlaylistDuration);
        playlist.setSongs(playlistSongs);
    }
    public static List<GenreDto> verifyTotalPercentage(PlaylistDto playlistDto) {
        List<GenreDto> genres = playlistDto.getGenreDtoList();
        int totalGenrePercentage = 0;
        for (GenreDto genreDto : genres) {
            totalGenrePercentage += genreDto.getPercentage();

        }
        if (totalGenrePercentage > 100) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Total genres percentage exceeded!");
        }
        return genres;
    }
}
