package com.main.movie.service;

import com.main.movie.error.ResourceNotFound;
import com.main.movie.integration.TheMovieDataBaseAPI;
import com.main.movie.model.*;
import com.main.movie.repository.LinkDAO;
import com.main.movie.repository.MovieDAO;
import com.main.movie.repository.RatingDAO;
import com.main.movie.util.SortOption;
import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
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
    private RatingDAO ratingDAO;
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
    public Flux<MovieDBResponse> getMoviesFromDB(Optional<String> sortPriority,
                                                 Optional<Boolean> sortByRating,
                                                 Optional<Boolean> sortByTitle,
                                                 Optional<String> genres,
                                                 Optional<Integer> limit,
                                                 Optional<Integer> page,
                                                 Optional<String> title){

        LinkedHashMap<Integer,Float> averageRating = ratingDAO.findAverageRatings();

        int start = page.orElse(1);
        if (start == 0 || start == 1) start = 0;
        else start = (start-1) * limit.orElse(10);

        Option<SortOption> sortPriorityOption = Option.none();
        Try<SortOption> optionParam = Try.of( () -> SortOption.valueOf(sortPriority.get().toUpperCase()));
        if(optionParam.isSuccess()) sortPriorityOption = Option.of(optionParam.get());

        // Variable needed to use into a lambda expression
        int finalStart = start;

        return getFluxMovieDBResponse(sortByRating, sortByTitle, genres, limit, title, averageRating, start, sortPriorityOption, finalStart);

    }

    private Flux<MovieDBResponse> getFluxMovieDBResponse(Optional<Boolean> sortByRating,
                                                         Optional<Boolean> sortByTitle,
                                                         Optional<String> genres,
                                                         Optional<Integer> limit,
                                                         Optional<String> title,
                                                         LinkedHashMap<Integer, Float> averageRating,
                                                         int start,
                                                         Option<SortOption> sortPriorityOption,
                                                         int finalStart){
        boolean compareByTitle = sortByTitle.isPresent() && sortByTitle.get() && ( !sortByRating.isPresent() || sortByRating.get().equals(false));
        boolean compareByRating = sortByRating.isPresent() && sortByRating.get() && ( !sortByTitle.isPresent() || sortByTitle.get().equals(false));
        boolean compareByRatingAndTitle = sortByRating.isPresent() && sortByRating.get() && sortByTitle.isPresent() && sortByTitle.get();
        boolean compareByTitleIsPriority = sortPriorityOption.isDefined() && sortPriorityOption.get().equals(SortOption.TITLE);

        Comparator<MovieDBResponse> higherPriorityComparator;
        Comparator<MovieDBResponse> lowerPriorityComparator;

        if(compareByTitle){
             higherPriorityComparator = Comparator.comparing(MovieDBResponse::getTitle);
             return findAllMoviesSortBy(genres, limit, title, finalStart, averageRating,higherPriorityComparator);
        }else if(compareByRating) {
            higherPriorityComparator = Comparator.comparing(MovieDBResponse::getAverageRating).reversed();
            return findAllMoviesSortBy(genres, limit, title, finalStart, averageRating, higherPriorityComparator);
        }else if(compareByRatingAndTitle){
            if(compareByTitleIsPriority){
                higherPriorityComparator = Comparator.comparing(MovieDBResponse::getTitle);
                lowerPriorityComparator = Comparator.comparing(MovieDBResponse::getAverageRating).reversed();
            }else{
                higherPriorityComparator = Comparator.comparing(MovieDBResponse::getAverageRating).reversed();
                lowerPriorityComparator = Comparator.comparing(MovieDBResponse::getTitle);
            }
            return findAllMoviesSortBy(genres, limit, title, finalStart, averageRating,higherPriorityComparator)
                    .sort(lowerPriorityComparator);
        }else{
            return findAllMovies(genres,limit,title,start,averageRating);
        }
    }

    private Flux<MovieDBResponse> findAllMovies(Optional<String> genres,
                                                Optional<Integer> limit,
                                                Optional<String> title,
                                                int start,
                                                LinkedHashMap<Integer,Float> averageRating){
        return Flux.fromIterable(movieDAO.findAll().stream()
                .map( movieDTO -> {
                        movieDTO.removeQuoteFromTitleIfExist();
                        return new MovieDBResponse(
                                movieDTO.getMovieId(),
                                movieDTO.getTitle(),
                                movieDTO.getGenres(),
                                (Optional.ofNullable(averageRating.get(movieDTO.getMovieId())).orElse((float) 0.0))/ (float)10);
                })
                .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                .filter(f -> !genres.isPresent() || f.getGenres().toLowerCase().contains(genres.get().toLowerCase()))
                .skip(start)
                .limit(limit.orElse(10))
                .collect(Collectors.toList()));
    }

    private Flux<MovieDBResponse> findAllMoviesSortBy(Optional<String> genres,
                                                      Optional<Integer> limit,
                                                      Optional<String> title,
                                                      int start,
                                                      LinkedHashMap<Integer,Float> averageRating,
                                                      Comparator<MovieDBResponse> comparator){
        return Flux.fromIterable(
            movieDAO.findAll().stream()
                    .map( movieDTO -> {
                        movieDTO.removeQuoteFromTitleIfExist();
                        return new MovieDBResponse(
                                movieDTO.getMovieId(),
                                movieDTO.getTitle(),
                                movieDTO.getGenres(),
                                (Optional.ofNullable(averageRating.get(movieDTO.getMovieId())).orElse((float) 0.0))/ (float)10);
                    })
                    .sorted(comparator)
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
    @Cacheable("getApiDetails")
    public Mono<MovieDetail> getApiDetails(Integer movieId){
        return Mono.from(
        Option.ofOptional(linkDAO.findTmdbId(movieId))
            .map( theTmdbId -> {

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
        }).getOrElse(Mono.error( () ->  new ResourceNotFound("The movie with id " + movieId + " doesn't exist"))));
    }

    @Override
    public Flux<MovieResponse> getMoviesData(Optional<String> sortPriority,
                                             Optional<Boolean> sortByRating,
                                             Optional<Boolean> sortByTitle,
                                             Optional<String> genres,
                                             Optional<Integer> limit,
                                             Optional<Integer> page,
                                             Optional<String> title){
        return getMoviesFromDB(sortPriority, sortByRating, sortByTitle, genres, limit, page, title)
                .flatMap( movieDTO ->
                      getApiDetails(movieDTO.getMovieId())
                            .map( movieDetail -> new MovieResponse(
                                    movieDTO.getMovieId(),
                                    movieDTO.getTitle(),
                                    movieDTO.getGenres(),
                                    movieDTO.getAverageRating(),
                                    movieDetail.getPoster_path(),
                                    movieDetail.getRelease_date(),
                                    movieDetail.getBudget(),
                                    movieDetail.getOverview(),
                                    movieDetail.getRuntime()))
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

    @Override
    public Mono<GenreResponse> getMoviesGenres() {
        return Mono.just(movieDAO.findAllGenres()
                    .stream()
                    .flatMap( genres ->  Arrays.stream(genres.split("\\|")))
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new)))
                .map(GenreResponse::new);
    }

    @Override
    public Mono<MovieRatingHistoryResponse> getMovieRatingHistory(Integer movieId) {
         return Mono.just(getRatings(movieId))
         .flatMap( ratingsByYear -> {
             if(!ratingsByYear.isEmpty())
                 return Mono.just( new MovieRatingHistoryResponse(movieId,ratingsByYear));
             else
                 return Mono.error( new ResourceNotFound("The movie with id " + movieId + " doesn't exist or isn't rated"));
         });
    }

    private List<RatingByYear> getRatings(Integer movieId){
        TreeMap<Integer,List<Float>> ratingMap = new TreeMap<>();
        ratingDAO.findAllRatingByMovieId(movieId)
                .forEach( ratingDTO -> {
                    RatingByYear ratingByYear = ratingDTO.getRatingByYear();
                    Integer key = ratingByYear.getYear();
                    List<Float> previousRatingList = Optional.ofNullable(ratingMap.get(key)).orElse(new ArrayList<>());
                    previousRatingList.add(ratingByYear.getRating());
                    ratingMap.put(key,previousRatingList);
                });
        return ratingMap.entrySet()
                .stream()
                .map( integerListEntry ->
                    new RatingByYear( integerListEntry.getKey(),(float) integerListEntry.getValue().stream().mapToDouble(a -> a)
                            .average().orElse(0.0))
                ).collect(Collectors.toList());
    }
}
