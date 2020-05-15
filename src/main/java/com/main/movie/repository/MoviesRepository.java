package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends CrudRepository<MovieDTO,Integer> {
    @Query( value = "select * from movie m order by m.title",
            nativeQuery = true)
    Iterable<MovieDTO> findAllOrderByTitle();

    @Query( value = "select * from movie limit ?1, ?2",
            nativeQuery = true)
    Iterable<MovieDTO> findByPage(Integer start, Integer limit);
}
