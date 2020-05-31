package com.main.movie.repository;

import com.main.movie.model.RatingDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<RatingDTO,Integer> {
    @Query( value = "SELECT movie_id, avg(rating) as rating FROM `wfh-movies-challenge`.rating group by movie_id;",
            nativeQuery = true)
    List<String> findAverageRatings();

    @Query( value = "select * from rating r where r.movie_id=?1 ",
            nativeQuery = true)
    List<RatingDTO> findAllRatingByMovieId(Integer movie_id);
}
