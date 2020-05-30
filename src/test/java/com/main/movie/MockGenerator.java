package com.main.movie;

import com.main.movie.model.*;

import java.util.*;

public class MockGenerator {
    public static Optional<MovieDTO> getMockMovie(){
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        return Optional.of( new MovieDTO(650, "movie1", "genre1|genre2", ratingDTOMock) );
    }

    public static List<MovieDTO> getMockMoviesDTO(){
        List<MovieDTO> movieListMock = new ArrayList<>();
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        for(int i=0; i<5; i++)
            movieListMock.add( new MovieDTO(i, i+":movie", "genre"+i, ratingDTOMock ));
        for(int i=5; i<10; i++)
            movieListMock.add( new MovieDTO(i, i+":dummy", "dummy"+i,  ratingDTOMock));
        return movieListMock;
    }

    public static List<MovieDBResponse> getMockMovies(){
        List<MovieDBResponse> movieListMock = new ArrayList<>();
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        for(int i=0; i<5; i++)
            movieListMock.add( new MovieDBResponse(i, i+":movie", "genre"+i, (float) 1.0) );
        for(int i=5; i<10; i++)
            movieListMock.add( new MovieDBResponse(i, i+":dummy", "dummy"+i,  (float) 1.0) );
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
        return Optional.ofNullable(862);
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

    public static MovieDetail getResultMovieDetail(){
        return new MovieDetail(862,
                "https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg",
                "1995-10-30",
                30000000,
                "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.",
                (float) 7.9);
    }

    public static CreditDTO getMockCredit(){
        List<CastDTO> castList = new ArrayList<>();
        castList.add( new CastDTO(14, "Woody (voice)", "Tom Hanks", "https://image.tmdb.org/t/p/w500/2gY92j2lkNHL2cThBhPmgXLd5PL.jpg") );
        castList.add( new CastDTO(15, "Buzz Lightyear (voice)", "Tim Allen", "") );

        CreditDTO credit = new CreditDTO(862, castList);
        return credit;
    }

}
