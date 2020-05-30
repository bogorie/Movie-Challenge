package com.main.movie.repository;

import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface LinkDAOInterface {
    @Cacheable("tmdbid")
    Optional<Integer> findTmdbId(Integer movie_id);
}
