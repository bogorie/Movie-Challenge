package com.main.movie.service;

import com.main.movie.model.CreditDTO;
import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.Response;
import com.main.movie.util.SortOption;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;


public interface MovieService {
    Optional<MovieDTO> getMovie(Integer movieId);
    List<MovieDTO> getMoviesFromDB(Optional<String> sort,
                                   Optional<String> genres,
                                   Optional<Integer> limit,
                                   Optional<Integer> page,
                                   Optional<String> title);

    Option<MovieDetail> getApiDetails(Integer movieId);

    List<Response> getMoviesData(Optional<String> sort,
                                 Optional<String> genres,
                                 Optional<Integer> limit,
                                 Optional<Integer> page,
                                 Optional<String> title);

    Option<CreditDTO> getApiCast(Integer movieId);

}
