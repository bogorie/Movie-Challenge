package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieDAOInterface {
    boolean save(MovieDTO movieDTO);

    @Cacheable("movies")
    List<MovieDTO> findAll();

    @Cacheable("movie")
    Optional<MovieDTO> findById(Integer id);

    @Cacheable("genre")
    Set<String> findAllGenres();
}
