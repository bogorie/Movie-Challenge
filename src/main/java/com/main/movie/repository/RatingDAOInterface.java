package com.main.movie.repository;

import org.springframework.cache.annotation.Cacheable;

import java.util.LinkedHashMap;

public interface RatingDAOInterface {
    @Cacheable("averageRating")
    LinkedHashMap<Integer,Float> findAverageRatings();
}
