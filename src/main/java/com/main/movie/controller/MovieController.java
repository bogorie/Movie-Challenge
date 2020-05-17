package com.main.movie.controller;

import com.main.movie.error.ResourceNotFound;
import com.main.movie.model.*;
import com.main.movie.service.MovieService;
import com.main.movie.service.MovieServiceImpl;
import com.main.movie.util.SortOption;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public MovieDTO getMovie(@PathVariable Integer movie_id) {
        Optional<MovieDTO> result = movieService.getMovie(movie_id);
        if( result.isPresent())
            return result.get();
        else
            throw new ResourceNotFound("The movie with id "+movie_id+" doesn't exist");

    }

    @GetMapping("/movie/detail/{movie_id}")
    public MovieDetail getdetails(@PathVariable Integer movie_id) {
        return movieService.getApiDetails(movie_id);
    }

    @GetMapping("/movie/casts/{movie_id}")
    public CreditDTO getApiCast(@PathVariable Integer movie_id) {
        return movieService.getApiCast(movie_id);
    }

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
