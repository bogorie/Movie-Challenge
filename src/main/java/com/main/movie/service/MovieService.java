package com.main.movie.service;

import com.main.movie.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface MovieService {
    Mono<MovieDTO> getMovie(Integer movieId);
    Flux<MovieDBResponse> getMoviesFromDB(Optional<String> sortPriority,
                                          Optional<Boolean> sortByRating,
                                          Optional<Boolean> sortByTitle,
                                          Optional<String> genres,
                                          Optional<Integer> limit,
                                          Optional<Integer> page,
                                          Optional<String> title);

    Mono<MovieDetail> getApiDetails(Integer movieId);

    Flux<MovieResponse> getMoviesData(Optional<String> sortPriority,
                                      Optional<Boolean> sortByRating,
                                      Optional<Boolean> sortByTitle,
                                      Optional<String> genres,
                                      Optional<Integer> limit,
                                      Optional<Integer> page,
                                      Optional<String> title);

    Mono<CreditDTO> getApiCast(Integer movieId);

    Mono<GenreResponse> getMoviesGenres();

    Mono<MovieRatingHistoryResponse> getMovieRatingHistory(Integer movieId);
}
