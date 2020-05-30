package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<MovieDTO,Integer> {
    @Query( value = "SELECT movie_id, avg(rating) as rating FROM `wfh-movies-challenge`.rating group by movie_id;",
            nativeQuery = true)
    List<String> findAverageRatings();
}
