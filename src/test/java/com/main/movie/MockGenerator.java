package com.main.movie;

import com.main.movie.model.MovieDTO;
import com.main.movie.model.RatingDTO;

import java.util.*;

public class MockGenerator {
    public static Optional<MovieDTO> getMockMovie(){
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        return Optional.of( new MovieDTO(650, "movie1", "genre1|genre2", ratingDTOMock) );
    }

    public static List<MovieDTO> getMockMovies(){
        List<MovieDTO> movieListMock = new ArrayList<>();
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        for(int i=0; i<5; i++)
            movieListMock.add( new MovieDTO(i, i+":movie", "genre"+i, ratingDTOMock) );
        for(int i=5; i<10; i++)
            movieListMock.add( new MovieDTO(i, i+":dummy", "dummy"+i, ratingDTOMock) );
        return movieListMock;
    }

    public static List<MovieDTO> movieWithFilter(){
        List<MovieDTO> movieListMock = new ArrayList<>();
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        for(int i=0; i<5; i++)
            movieListMock.add( new MovieDTO(i, i+":movie", "genre"+i, ratingDTOMock) );
        return movieListMock;
    }

}
