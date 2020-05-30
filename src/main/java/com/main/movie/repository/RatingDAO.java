package com.main.movie.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class RatingDAO implements RatingDAOInterface {
    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public LinkedHashMap<Integer,Float> findAverageRatings(){
        LinkedHashMap<Integer,Float> response = new LinkedHashMap<>();
        ratingRepository.findAverageRatings().stream()
                .map(value -> value.split(","))
                .forEach(array -> response.put(Integer.valueOf(array[0]),Float.valueOf(array[1])));
        return response;
    }

}
