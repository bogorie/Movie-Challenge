package com.main.movie.repository;

import com.main.movie.model.LinkDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends CrudRepository<LinkDTO, Integer> {
    @Query( value = "select l.tmdb_id from link l where l.movie_id=?1 LIMIT 1 ",
            nativeQuery = true)
    Optional<Integer> findTmdbId(Integer movie_id);
}
