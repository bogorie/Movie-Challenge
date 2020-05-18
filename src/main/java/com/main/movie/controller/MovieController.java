package com.main.movie.controller;

import com.main.movie.model.CreditDTO;
import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.MovieResponse;
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

    @GetMapping("/movie/{movie_id}")
    public Mono<MovieDTO> getMovie(@PathVariable Integer movie_id) {
        return movieService.getMovie(movie_id);

    }

    @GetMapping("/movie/detail/{movie_id}")
    public Mono<MovieDetail> getdetails(@PathVariable Integer movie_id) {
        return movieService.getApiDetails(movie_id);
    }

    @GetMapping("/movie/casts/{movie_id}")
    public Mono<CreditDTO> getApiCast(@PathVariable Integer movie_id) {
        return movieService.getApiCast(movie_id);
    }

    @GetMapping("/moviesDB")
    public Flux<MovieDTO> getMovies(@RequestParam(required = false) Optional<String> sort,
                                    @RequestParam(required = false) Optional<String> genres,
                                    @RequestParam(required = false) Optional<Integer> limit,
                                    @RequestParam(required = false) Optional<Integer> page,
                                    @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesFromDB(sort, genres, limit, page, title);
    }

    @GetMapping("/movies")
    public Flux<MovieResponse> getData(@RequestParam(required = false) Optional<String> sort,
                                       @RequestParam(required = false) Optional<String> genres,
                                       @RequestParam(required = false) Optional<Integer> limit,
                                       @RequestParam(required = false) Optional<Integer> page,
                                       @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesData(sort, genres, limit, page, title);
    }

}
