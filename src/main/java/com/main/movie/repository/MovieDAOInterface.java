package com.main.movie.repository;

import com.main.movie.model.MovieDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieDAOInterface {
    boolean save(MovieDTO movieDTO);
    List<MovieDTO> findAll();
    Optional<MovieDTO> findById(Integer id);
    Set<String> findAllGenres();
}
