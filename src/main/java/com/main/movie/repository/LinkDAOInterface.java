package com.main.movie.repository;

import java.util.Optional;

public interface LinkDAOInterface {
    Optional<Integer> findTmdbId(Integer movie_id);
}
