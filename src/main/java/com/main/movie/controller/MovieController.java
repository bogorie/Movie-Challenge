package com.main.movie.controller;

import com.main.movie.model.*;
import com.main.movie.service.MovieService;
import com.main.movie.service.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequestMapping(path="/api")
@RestController
@CrossOrigin(
        origins = {
                "https://secret-beyond-17324.herokuapp.com/",
                "http://localhost:3000"
        }
)
public class MovieController {
    private MovieService movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieServiceImp){
        movieService = movieServiceImp;
    }

    @GetMapping("/movie/{movie_id}")
    public Mono<MovieDTO> getMovie(@PathVariable Integer movie_id) {
        return movieService.getMovie(movie_id);
    }

    @GetMapping("/movie/detail/{movie_id}")
    public Mono<MovieDetail> getMovieDetails(@PathVariable Integer movie_id) {
        return movieService.getApiDetails(movie_id);
    }

    @GetMapping("/movie/casts/{movie_id}")
    public Mono<CreditDTO> getApiCast(@PathVariable Integer movie_id) {
        return movieService.getApiCast(movie_id);
    }

    @GetMapping("/movie/rating/{movie_id}")
    public Mono<MovieRatingHistoryResponse> getMovieRatingHistory(@PathVariable Integer movie_id){
        return movieService.getMovieRatingHistory(movie_id);
    }

    @GetMapping("/moviesDB")
    public Flux<MovieDBResponse> getMovies(@RequestParam(required = false) Optional<String> sortPriority,
                                           @RequestParam(required = false) Optional<Boolean> sortByRating,
                                           @RequestParam(required = false) Optional<Boolean> sortByTitle,
                                           @RequestParam(required = false) Optional<String> genres,
                                           @RequestParam(required = false) Optional<Integer> limit,
                                           @RequestParam(required = false) Optional<Integer> page,
                                           @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesFromDB(sortPriority,sortByRating,sortByTitle, genres, limit, page, title);
    }

    @GetMapping("/movies")
    public Flux<MovieResponse> getMovieData(@RequestParam(required = false) Optional<String> sortPriority,
                                            @RequestParam(required = false) Optional<Boolean> sortByRating,
                                            @RequestParam(required = false) Optional<Boolean> sortByTitle,
                                            @RequestParam(required = false) Optional<String> genres,
                                            @RequestParam(required = false) Optional<Integer> limit,
                                            @RequestParam(required = false) Optional<Integer> page,
                                            @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesData(sortPriority,sortByRating,sortByTitle, genres, limit, page, title);
    }

    @GetMapping("/movies/genres")
    public Mono<GenreResponse> getMoviesGenres(){
        return movieService.getMoviesGenres();
    }
}
