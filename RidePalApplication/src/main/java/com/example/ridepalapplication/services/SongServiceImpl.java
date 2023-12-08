package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Artist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.SongRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> findAll(Integer page, Integer pageSize, String songTitle,String artist) {
        PageRequest pageable = PageRequest.of(page, pageSize);

        Specification<Song> songTitleSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("title").as(String.class)),
                        "%" + songTitle + "%");

        Specification<Song> songArtistSpecification = (root, query, criteriaBuilder) -> {
            Join<Song, Artist> artistJoin = root.join("artist", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.upper(artistJoin.get("name").as(String.class)),
                    "%" + artist + "%");
        };

        Specification<Song> songSpecification = Specification.allOf(songTitleSpecification,songArtistSpecification);

        return songRepository.findAll(songSpecification, pageable.withSort(Sort.Direction.DESC,"rank")).getContent();
    }

    @Override
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Optional<Song> getById(Long id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isEmpty()) {
            throw new EntityNotFoundException("Song", id);
        } else return song;
    }

    @Override
    public Song getByTitleAndArtist(String name, String artist) {
        List<Song> songs = songRepository.findByTitleAndArtistName(name,artist);
       if(songs.isEmpty()){
           throw  new EntityNotFoundException("Song","name",name);
       }
       return songs.get(0);
    }
}
