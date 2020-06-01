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
public class MovieController {
    private MovieService movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieServiceImp){
        movieService = movieServiceImp;
    }

    @CrossOrigin
    @GetMapping("/movie/{movie_id}")
    public Mono<MovieDTO> getMovie(@PathVariable Integer movie_id) {
        return movieService.getMovie(movie_id);

    }

    @CrossOrigin
    @GetMapping("/movie/detail/{movie_id}")
    public Mono<MovieDetail> getMovieDetails(@PathVariable Integer movie_id) {
        return movieService.getApiDetails(movie_id);
    }

    @CrossOrigin
    @GetMapping("/movie/casts/{movie_id}")
    public Mono<CreditDTO> getApiCast(@PathVariable Integer movie_id) {
        return movieService.getApiCast(movie_id);
    }

    @CrossOrigin
    @GetMapping("/movie/rating/{movie_id}")
    public Mono<MovieRatingHistoryResponse> getMovieRatingHistory(@PathVariable Integer movie_id){
        return movieService.getMovieRatingHistory(movie_id);
    }

    @CrossOrigin
    @GetMapping("/moviesDB")
    public Flux<MovieDBResponse> getMovies(@RequestParam(required = false) Optional<String> sort,
                                    @RequestParam(required = false) Optional<String> genres,
                                    @RequestParam(required = false) Optional<Integer> limit,
                                    @RequestParam(required = false) Optional<Integer> page,
                                    @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesFromDB(sort, genres, limit, page, title);
    }

    @CrossOrigin
    @GetMapping("/movies")
    public Flux<MovieResponse> getMovieData(@RequestParam(required = false) Optional<String> sort,
                                            @RequestParam(required = false) Optional<String> genres,
                                            @RequestParam(required = false) Optional<Integer> limit,
                                            @RequestParam(required = false) Optional<Integer> page,
                                            @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesData(sort, genres, limit, page, title);
    }

    @CrossOrigin
    @GetMapping("/movies/genres")
    public Mono<GenreResponse> getMoviesGenres(){
        return movieService.getMoviesGenres();
    }
}
