package com.main.movie.controller;

import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.Response;
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

    @GetMapping("/movies")
    public List<MovieDTO> getMovies(@RequestParam(required = false) Optional<String> sort,
                                    @RequestParam(required = false) Optional<String> genres,
                                    @RequestParam(required = false) Optional<Integer> limit,
                                    @RequestParam(required = false) Optional<Integer> page,
                                    @RequestParam(required = false) Optional<String> title){
        return movieService.getMovies(sort, genres, limit, page, title);
    }

    @GetMapping("/movie/detail/{tmdb_id}")
    public MovieDetail getdetails(@PathVariable Integer tmdb_id) {
        return movieService.getApiDetails(tmdb_id);
    }

    @GetMapping("/data")
    public List<Response> getData(@RequestParam(required = false) Optional<String> sort,
                                  @RequestParam(required = false) Optional<String> genres,
                                  @RequestParam(required = false) Optional<Integer> limit,
                                  @RequestParam(required = false) Optional<Integer> page,
                                  @RequestParam(required = false) Optional<String> title){
        return movieService.getData(sort, genres, limit, page, title);
    }

}
