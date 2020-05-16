package com.main.movie.controller;

import com.main.movie.model.*;
import com.main.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {
    @Autowired
    MovieService movieService;

    @GetMapping("/movie/{movie_id}")
    public MovieDTO getMovie(@PathVariable Integer movie_id) {
        return movieService.getMovie(movie_id);
    }

    @GetMapping("/movie/detail/{movie_id}")
    public MovieDetail getdetails(@PathVariable Integer movie_id) { return movieService.getApiDetails(movie_id); }

    @GetMapping("/movie/casts/{movie_id}")
    public CreditDTO getApiCast(@PathVariable Integer movie_id) { return movieService.getApiCast(movie_id); }

    @GetMapping("/moviesDB")
    public List<MovieDTO> getMovies(@RequestParam(required = false) Optional<String> sort,
                                    @RequestParam(required = false) Optional<String> genres,
                                    @RequestParam(required = false) Optional<Integer> limit,
                                    @RequestParam(required = false) Optional<Integer> page,
                                    @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesFromDB(sort, genres, limit, page, title);
    }

    @GetMapping("/movies")
    public List<Response> getData(@RequestParam(required = false) Optional<String> sort,
                                  @RequestParam(required = false) Optional<String> genres,
                                  @RequestParam(required = false) Optional<Integer> limit,
                                  @RequestParam(required = false) Optional<Integer> page,
                                  @RequestParam(required = false) Optional<String> title){
        return movieService.getMoviesData(sort, genres, limit, page, title);
    }

}
