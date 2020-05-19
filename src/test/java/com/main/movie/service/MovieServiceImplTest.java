package com.main.movie.service;

import com.main.movie.MockGenerator;
import com.main.movie.model.MovieDTO;
import com.main.movie.model.RatingDTO;
import com.main.movie.repository.MoviesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieServiceImplTest {
    @Autowired
    private MovieService movieService;

    @MockBean
    private MoviesRepository moviesRepository;

    @BeforeEach
    void setMoviesRepository(){
        Mockito.when(moviesRepository.findAll())
                .thenReturn(MockGenerator.getMockMovies());
        Mockito.when(moviesRepository.findById(MockGenerator.getMockMovie().get().getMovieId()))
                .thenReturn(MockGenerator.getMockMovie());
    }

    @Test
    void getMovie() {
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        MovieDTO result = new MovieDTO(650, "movie1", "genre1|genre2", ratingDTOMock);

        Mono<MovieDTO> movie = movieService.getMovie(650);
        assertNotNull(movie);
        assertEquals(movie.block().getMovieId(), result.getMovieId());
    }

    @Test
    void getMoviesFromDBNotQueryParam() {
        List<MovieDTO> result = MockGenerator.getMockMovies();

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieDTO> moviesList = movies.collectList().block();

        for(int i=0; i<result.size(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i).getMovieId());
        }
    }

    @Test
    void getMoviesFromDBSortTitle() {
        Optional<String> sort = Optional.of("title");

        List<MovieDTO> result = MockGenerator.getMockMovies();
        result.sort(Comparator.comparing(MovieDTO::getTitle));

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(sort, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieDTO> moviesList = movies.collectList().block();

        for(int i=0; i<result.size(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i).getMovieId());
        }
    }

    @Test
    void getMoviesFromDBFilterGenre() {
        Optional<String> genres = Optional.of("genre");
        Optional<String> sort = Optional.of("title");

        List<MovieDTO> result = MockGenerator.movieWithFilter();
        result.sort(Comparator.comparing(MovieDTO::getTitle));

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(sort, genres, Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieDTO> moviesList = movies.collectList().block();

        assertEquals(moviesList.size(), result.size());
        for(int i=0; i<result.size(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i).getMovieId());
        }
    }

    @Test
    void getMoviesFromDBFilterGenreAndSortTitle() {
        Optional<String> genres = Optional.of("genre");

        List<MovieDTO> result = MockGenerator.movieWithFilter();

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(Optional.empty(), genres, Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieDTO> moviesList = movies.collectList().block();

        assertEquals(moviesList.size(), result.size());
        for(int i=0; i<result.size(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i).getMovieId());
        }
    }

    @Test
    void getMoviesFromDBWithPageAndLimit() {
        Optional<Integer> limit = Optional.of(5);
        Optional<Integer> page = Optional.of(2);

        List<MovieDTO> result = MockGenerator.getMockMovies();

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(Optional.empty(), Optional.empty(), limit, page, Optional.empty());
        List<MovieDTO> moviesList = movies.collectList().block();

        assertEquals(moviesList.size(), limit.get());
        for(int i=0; i<limit.get(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i+limit.get()).getMovieId());
        }
    }

    @Test
    void getMoviesFromDBSearchFromTitle() {
        Optional<String> title = Optional.of("movie");

        List<MovieDTO> result = MockGenerator.movieWithFilter();

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), title);
        List<MovieDTO> moviesList = movies.collectList().block();

        for(int i=0; i<result.size(); i++){
            assertEquals(moviesList.get(i).getMovieId(), result.get(i).getMovieId());
        }
    }

    @Test
    void getApiDetails() {
    }

    @Test
    void getMoviesData() {
    }

    @Test
    void getApiCast() {
    }
}