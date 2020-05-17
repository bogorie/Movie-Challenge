package com.main.movie.service;

import com.main.movie.model.CreditDTO;
import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.Response;
import io.vavr.control.Option;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Mono<MovieDTO> getMovie(Integer movieId);
    Flux<MovieDTO> getMoviesFromDB(Optional<String> sort,
                                   Optional<String> genres,
                                   Optional<Integer> limit,
                                   Optional<Integer> page,
                                   Optional<String> title);

    Mono<MovieDetail> getApiDetails(Integer movieId);

    Flux<Response> getMoviesData(Optional<String> sort,
                                 Optional<String> genres,
                                 Optional<Integer> limit,
                                 Optional<Integer> page,
                                 Optional<String> title);

    Mono<CreditDTO> getApiCast(Integer movieId);

}
