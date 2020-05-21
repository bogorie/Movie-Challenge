package com.main.movie.service;

import com.main.movie.error.ResourceNotFound;
import com.main.movie.integration.TheMovieDataBaseAPI;
import com.main.movie.model.CreditDTO;
import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.MovieResponse;
import com.main.movie.repository.LinkDAO;
import com.main.movie.repository.MovieDAO;
import com.main.movie.util.SortOption;
import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;
import static io.vavr.Predicates.is;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private LinkDAO linkDAO;
    @Autowired
    private Environment env;

    @Autowired
    private TheMovieDataBaseAPI theMovieDataBaseAPIImpl;

    private Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Override
    public Mono<MovieDTO> getMovie(Integer movieId){
        Optional<MovieDTO> result = movieDAO.findById(movieId);
        return result.map(Mono::just).orElseGet(() -> Mono.error( new ResourceNotFound("The movie with id " + movieId + " doesn't exist")));
    }

    @Override
    public Flux<MovieDTO> getMoviesFromDB(Optional<String> sort,
                                          Optional<String> genres,
                                          Optional<Integer> limit,
                                          Optional<Integer> page,
                                          Optional<String> title){
        int start = page.orElse(1);
        if (start == 0 || start == 1) start = 0;
        else start = (start-1) * limit.orElse(10);

        Option<SortOption> sortOption = Option.none();
        Try<SortOption> optionParam = Try.of( () -> SortOption.valueOf(sort.get().toUpperCase()));
        if(optionParam.isSuccess()) sortOption = Option.of(optionParam.get());

        // Variable needed to use into a lambda expression
        int finalStart = start;
        return API.Match(sortOption).of(
          Case($Some($()), value ->
                  API.Match(value).of(
                          Case($(is(SortOption.TITLE)),findAllMoviesSortByTitle(genres,limit,title, finalStart)),
                          Case($(),findAllMovies(genres,limit,title, finalStart)))),
          Case($None(), findAllMovies(genres,limit,title,start)));
    }

    private Flux<MovieDTO> findAllMovies(Optional<String> genres,
                                         Optional<Integer> limit,
                                         Optional<String> title,
                                         int start){
        return Flux.fromIterable(movieDAO.findAll().stream()
                .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                .filter(f -> !genres.isPresent() || f.getGenres().toLowerCase().contains(genres.get().toLowerCase()))
                .skip(start)
                .limit(limit.orElse(10))
                .collect(Collectors.toList()));
    }

    private Flux<MovieDTO> findAllMoviesSortByTitle(Optional<String> genres,
                                                    Optional<Integer> limit,
                                                    Optional<String> title,
                                                    int start){
        return Flux.fromIterable(movieDAO.findAll().stream()
                .sorted(Comparator.comparing(MovieDTO::getTitle))
                .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                .filter(f -> {
                    if (!genres.isPresent())
                        return true;
                    boolean flag = false;
                    String[] g = genres.get().split(",");
                    for (String s : g) flag = f.getGenres().toLowerCase().contains(s.toLowerCase());
                    return flag;
                })
                .skip(start)
                .limit(limit.orElse(10))
                .collect(Collectors.toList()));
    }

    @Override
    public Mono<MovieDetail> getApiDetails(Integer movieId){
        Option<Integer> tmdbId = Option.ofOptional(linkDAO.findTmdbId(movieId));
        return tmdbId.map( theTmdbId -> {

            String apiKey = env.getProperty("api.key");
            String uri = theTmdbId.toString() + apiKey;
            return theMovieDataBaseAPIImpl.call(HttpMethod.GET,uri)
                    .bodyToMono( new ParameterizedTypeReference<MovieDetail>(){})
                    .map( movieDetail -> {
                            String poster_path = "";
                            if (movieDetail.getPoster_path() != null)
                                poster_path = env.getProperty("api.poster.path") + movieDetail.getPoster_path();
                            movieDetail.setPoster_path(poster_path);
                        return movieDetail;
                        });
        }).getOrElse(Mono.error( () ->  new ResourceNotFound("The movie with id " + movieId + " doesn't exist")));
    }

    @Override
    public Flux<MovieResponse> getMoviesData(Optional<String> sort,
                                             Optional<String> genres,
                                             Optional<Integer> limit,
                                             Optional<Integer> page,
                                             Optional<String> title){
        return getMoviesFromDB(sort, genres, limit, page, title)
                .flatMap( movieDTO ->
                      getApiDetails(movieDTO.getMovieId())
                            .map( movieDetail -> new MovieResponse(
                                    movieDTO.getMovieId(),
                                    movieDTO.getTitle(),
                                    movieDTO.getGenres(),
                                    movieDetail.getVote_average(),
                                    movieDetail.getPoster_path(),
                                    movieDetail.getRelease_date(),
                                    movieDetail.getBudget(),
                                    movieDetail.getOverview()))
                            .onErrorResume( RuntimeException.class, __ ->  Mono.empty())
                );
    }

    @Override
    public Mono<CreditDTO> getApiCast(Integer movieId){
        Option<Integer> tmdbId = Option.ofOptional(linkDAO.findTmdbId(movieId));
        return tmdbId.map( theTmdbId -> {

            String apiKey = env.getProperty("api.key");
            String uri = theTmdbId.toString() + "/credits" + apiKey;
            return theMovieDataBaseAPIImpl.call(HttpMethod.GET,uri)
                    .bodyToMono( new ParameterizedTypeReference<CreditDTO>(){})
                    .map( creditDTO -> {
                            creditDTO.getCast().stream().forEach( castDTO -> {
                                if (castDTO.getCharacter() != null) {
                                    String profile_path = "";
                                    if (castDTO.getProfile_path() != null)
                                        profile_path = env.getProperty("api.poster.path") + castDTO.getProfile_path();
                                    castDTO.setProfile_path(profile_path);
                                }else creditDTO.getCast().remove(castDTO);
                            });
                            return creditDTO;
                    });
        }).getOrElse(Mono.error( () ->  new ResourceNotFound("The movie with id " + movieId + " doesn't exist")));
    }
}
