package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends CrudRepository<MovieDTO,Integer> {
}
