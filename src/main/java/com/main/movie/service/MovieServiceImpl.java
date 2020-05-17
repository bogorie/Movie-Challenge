package com.main.movie.service;

import com.main.movie.model.*;
import com.main.movie.repository.LinkDAO;
import com.main.movie.repository.MovieDAO;
import com.main.movie.util.SortOption;
import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;
import static io.vavr.Predicates.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private LinkDAO linkDAO;

    @Override
    public Optional<MovieDTO> getMovie(Integer movieId){
        return  movieDAO.findById(movieId);
    }

    @Override
    public List<MovieDTO> getMoviesFromDB(Optional<String> sort,
                                          Optional<String> genres,
                                          Optional<Integer> limit,
                                          Optional<Integer> page,
                                          Optional<String> title){
        int start = page.orElse(1);
        if (start != 0 && start != 1) start += limit.orElse(10);
        else start = 0;

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

    private List<MovieDTO> findAllMovies(Optional<String> genres,
                                         Optional<Integer> limit,
                                         Optional<String> title,
                                         int start){
        return movieDAO.findAll().stream()
                .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                .filter(f -> !genres.isPresent() || f.getGenres().toLowerCase().contains(genres.get().toLowerCase()))
                .skip(start)
                .limit(limit.orElse(10))
                .collect(Collectors.toList());
    }

    private List<MovieDTO> findAllMoviesSortByTitle(Optional<String> genres,
                                                    Optional<Integer> limit,
                                                    Optional<String> title,
                                                    int start){
        return movieDAO.findAll().stream()
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
                .collect(Collectors.toList());
    }

    @Override
    public MovieDetail getApiDetails(Integer movieId){
        Optional<Integer> tmdbId = linkDAO.findTmdbId(movieId);

        if(tmdbId.isPresent()){
            RestTemplate restTemplate = new RestTemplate();

            String host = "https://api.themoviedb.org/3/movie/";
            String apiKey = "?api_key=16a34aee6285afd1a01796ab3a6a45bb";
            String uri = host + tmdbId.get().toString() + apiKey;

            try {
                MovieDetail movieDetail = restTemplate.getForObject(uri, MovieDetail.class);
                if (movieDetail != null) {
                    String poster_path = "";
                    if (movieDetail.getPoster_path() != null)
                        poster_path = "https://image.tmdb.org/t/p/w500" + movieDetail.getPoster_path();
                    movieDetail.setPoster_path(poster_path);

                    return movieDetail;
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return new MovieDetail();
                }
                throw e;
            }
        }
        return new MovieDetail();
    }

    @Override
    public List<Response> getMoviesData(Optional<String> sort,
                                        Optional<String> genres,
                                        Optional<Integer> limit,
                                        Optional<Integer> page,
                                        Optional<String> title){
        List<Response> responseList = new ArrayList<>();
        List<MovieDTO> movieDTOList = this.getMoviesFromDB(sort, genres, limit, page, title);

        for(MovieDTO m : movieDTOList){
            MovieDetail movieDetail = getApiDetails(m.getMovieId());

            Response response = new Response(m.getMovieId(), m.getTitle(), m.getGenres(), movieDetail.getVote_average(),
                    movieDetail.getPoster_path(), movieDetail.getRelease_date(), movieDetail.getBudget(), movieDetail.getOverview());
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public CreditDTO getApiCast(Integer movieId){
        Optional<Integer> tmdbId = linkDAO.findTmdbId(movieId);

        if(tmdbId.isPresent()){
            RestTemplate restTemplate = new RestTemplate();

            String host = "https://api.themoviedb.org/3/movie/";
            String apiKey = "?api_key=16a34aee6285afd1a01796ab3a6a45bb";
            String uri = host + tmdbId.get().toString() + "/credits" + apiKey;

            try {
                CreditDTO creditDTO = restTemplate.getForObject(uri, CreditDTO.class);
                if (creditDTO != null) {
                    for (CastDTO castDTO : creditDTO.getCast()) {
                        if (castDTO.getCharacter() != null) {
                            String profile_path = "";
                            if (castDTO.getProfile_path() != null)
                                profile_path = "https://image.tmdb.org/t/p/w500" + castDTO.getProfile_path();
                            castDTO.setProfile_path(profile_path);
                        } else
                            creditDTO.getCast().remove(castDTO);
                    }
                    return creditDTO;
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return new CreditDTO();
                }
                throw e;
            }
        }

        return new CreditDTO();
    }
}
