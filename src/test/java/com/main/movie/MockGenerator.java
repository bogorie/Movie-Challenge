package com.main.movie;

import com.main.movie.model.*;
import reactor.core.publisher.Mono;

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

    public static List<MovieResponse> getMockMoviesDetail(){
        List<MovieResponse> movieResponseList = new ArrayList<>();
        for(int i=0; i<5; i++)
            movieResponseList.add(
                    new MovieResponse(i, i+":movie", "genre"+i, (float) 4.5, "http://mockHost.com/image/mockImage.jpg", "2000-1-1", 30000, "mock overview for movie "+i)
            );

        return movieResponseList;
    }

    public static Optional<Integer> getMockTmdbId(){
        return Optional.of(100);
    }

    public static Optional<MovieDetail> getMockMovieDetail(){
        MovieDetail movieDetail = new MovieDetail(650, "http://mockHost.com/image/mockImage.jpg", "2000-1-1", 30000, "mock overview", (float) 4.5);
        return Optional.of(movieDetail);
    }

    public static Optional<CreditDTO> getMockCast(){
        List<CastDTO> castDTOList = new ArrayList<>();
        castDTOList.add( new CastDTO(1, "dummy character", "actor1", "http://mockHost.com/image/mockImage.jpg") );
        CreditDTO creditDTO = new CreditDTO(1, castDTOList);

        return Optional.of(creditDTO);
    }

}
