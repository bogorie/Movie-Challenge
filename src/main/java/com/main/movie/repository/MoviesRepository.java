package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MoviesRepository extends CrudRepository<MovieDTO,Integer> {
    @Query( value = "SELECT genres FROM movie",
            nativeQuery = true)
    Set<String> findAllGenres();
}
