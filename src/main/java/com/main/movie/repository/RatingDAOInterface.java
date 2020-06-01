package com.main.movie.repository;

import com.main.movie.model.RatingDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.LinkedHashMap;
import java.util.List;

public interface RatingDAOInterface {
    @Cacheable("averageRating")
    LinkedHashMap<Integer,Float> findAverageRatings();

    @Cacheable("ratingByMovieId")
    List<RatingDTO> findAllRatingByMovieId(Integer movieId);
}
