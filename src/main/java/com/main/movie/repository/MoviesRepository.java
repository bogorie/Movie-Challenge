package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.data.repository.CrudRepository;

public interface MoviesRepository extends CrudRepository<MovieDTO,Integer> {

}
