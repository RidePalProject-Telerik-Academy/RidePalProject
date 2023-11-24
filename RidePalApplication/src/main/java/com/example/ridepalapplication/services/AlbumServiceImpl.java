package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Album;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.repositories.AlbumRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public Optional<Album> getById(Long id) {
        Optional<Album> album = albumRepository.findById(id);
        if(album.isEmpty()){
            throw new EntityNotFoundException("Album",id);
        }
        return album;
    }

    @Override
    public List<Album> findAll(Integer page, Integer pageSize, String albumTitle, String genre) {

        PageRequest pageable = PageRequest.of(page, pageSize);

        Specification<Album> albumTitleSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("name").as(String.class)),
                        "%" + albumTitle + "%");

        Specification<Album> albumGenreSpecification = (root, query, criteriaBuilder) -> {
            Join<Album, Genre> artistJoin = root.join("genre", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.upper(artistJoin.get("name").as(String.class)),
                    "%" + genre + "%");
        };


        Specification<Album> albumSpecifications = Specification.allOf(albumGenreSpecification,albumTitleSpecification);

       return albumRepository.findAll(albumSpecifications,pageable).getContent();
    }
}
