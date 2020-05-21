package com.main.movie.service;

import com.main.movie.MockGenerator;
import com.main.movie.config.TestConfigurationSuite;
import com.main.movie.model.*;
import com.main.movie.repository.LinkRepository;
import com.main.movie.repository.MoviesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfigurationSuite.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieServiceImplTest {
    @Autowired
    private MovieService movieService;

    @MockBean
    private MoviesRepository moviesRepository;

    @MockBean
    private LinkRepository linkRepository;

    private int mockMovieId;


    @BeforeEach
    void setMoviesRepository(){
        this.mockMovieId = 650;

        Mockito.when(moviesRepository.findAll())
                .thenReturn(MockGenerator.getMockMovies());
        Mockito.when(moviesRepository.findById(MockGenerator.getMockMovie().get().getMovieId()))
                .thenReturn(MockGenerator.getMockMovie());

        Mockito.when(linkRepository.findTmdbId(mockMovieId))
                .thenReturn(MockGenerator.getMockTmdbId());
    }

    @Test
    void getMovie() {
        Set<RatingDTO> ratingDTOMock = new HashSet<>();
        MovieDTO result = new MovieDTO(mockMovieId, "movie1", "genre1|genre2", ratingDTOMock);

        Mono<MovieDTO> movie = movieService.getMovie(mockMovieId);
        assertNotNull(movie);
        assertEquals(movie.block().getMovieId(), result.getMovieId());
    }

    @Test
    void getMovieNotFound() {
        Mono<MovieDTO> movie = movieService.getMovie(1);
        assertEquals(movie.toString(), "MonoError");
    }

    @Test
    void getMoviesFromDBNotQueryParam() {
        List<MovieDTO> result = MockGenerator.getMockMovies();

        Flux<MovieDTO> movies = movieService.getMoviesFromDB(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieDTO> moviesList = movies.toStream().collect(Collectors.toList());

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
        Mono<MovieDetail> movieDetail = movieService.getApiDetails(mockMovieId);
        MovieDetail result = MockGenerator.getResultMovieDetail();

        assertEquals(result.getId(), movieDetail.block().getId());
        assertEquals(result.getPoster_path(), movieDetail.block().getPoster_path());
        assertEquals(result.getBudget(), movieDetail.block().getBudget());
        assertEquals(result.getOverview(), movieDetail.block().getOverview());
        assertEquals(result.getRelease_date(), movieDetail.block().getRelease_date());
        assertEquals(result.getVote_average(), movieDetail.block().getVote_average());
    }

    @Test
    void getMoviesData() {
        List<MovieDTO> resultList = MockGenerator.getMockMovies();
        MovieDetail resultDetail = MockGenerator.getResultMovieDetail();

        for(int i=0; i<10; i++)
            Mockito.when(linkRepository.findTmdbId(i))
                    .thenReturn(MockGenerator.getMockTmdbId());

        Flux<MovieResponse> movieResponse = movieService.getMoviesData(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        List<MovieResponse> movieResponseList = movieResponse.toStream().collect(Collectors.toList());

        assertNotNull(movieResponseList);
        assertEquals(resultList.size(), movieResponseList.size());
        for(int i=0; i<10; i++){
            assertEquals(resultList.get(i).getMovieId(), movieResponseList.get(i).getMovieId());
            assertEquals(resultList.get(i).getTitle(), movieResponseList.get(i).getTitle());
            assertEquals(resultList.get(i).getGenres(), movieResponseList.get(i).getGenres());
            assertEquals(resultDetail.getVote_average(), movieResponseList.get(i).getRating());
            assertEquals(resultDetail.getPoster_path(), movieResponseList.get(i).getPoster_path());
            assertEquals(resultDetail.getRelease_date(), movieResponseList.get(i).getRelease_date());
            assertEquals(resultDetail.getBudget(), movieResponseList.get(i).getBudget());
            assertEquals(resultDetail.getOverview(), movieResponseList.get(i).getOverview());
        }
    }

    @Test
    void getApiCast() {
        CreditDTO result = MockGenerator.getMockCredit();
        Mono<CreditDTO> credit = movieService.getApiCast(mockMovieId);

        assertNotNull(credit.block());
        assertEquals(result.getCast().size(), credit.block().getCast().size());
        assertEquals(result.getId(), credit.block().getId());

        for(int i=0; i<result.getCast().size(); i++){
            assertEquals(result.getCast().get(i).getProfile_path(), credit.block().getCast().get(i).getProfile_path());
            assertEquals(result.getCast().get(i).getCharacter(), credit.block().getCast().get(i).getCharacter());
            assertEquals(result.getCast().get(i).getCast_id(), credit.block().getCast().get(i).getCast_id());
            assertEquals(result.getCast().get(i).getName(), credit.block().getCast().get(i).getName());
        }
    }
}