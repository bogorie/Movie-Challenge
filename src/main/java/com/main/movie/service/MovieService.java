package com.main.movie.service;

import com.main.movie.model.MovieDTO;
import com.main.movie.model.MovieDetail;
import com.main.movie.model.Response;
import com.main.movie.repository.LinkDAO;
import com.main.movie.repository.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private LinkDAO linkDAO;

    public MovieDTO getMovie(Integer movieId){
        MovieDTO emptyMovie = new MovieDTO();
        Optional<MovieDTO> movie = movieDAO.findById(movieId);
        return movie.orElse(emptyMovie);
    }

    public List<MovieDTO> getMovies(Optional<String> sort, Optional<String> genres, Optional<Integer> limit, Optional<Integer> page, Optional<String> title){
        int start = page.orElse(1);
        if (start != 0 && start != 1) start += limit.orElse(10) - 1;
        else start = 0;

        if(sort.isPresent() && sort.get().equalsIgnoreCase("title")){
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
                    .limit(limit.orElse(10) - 1)
                    .collect(Collectors.toList());
        }
        else{
            return movieDAO.findAll().stream()
                    .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                    .filter(f -> !genres.isPresent() || f.getGenres().toLowerCase().contains(genres.get().toLowerCase()))
                    .skip(start)
                    .limit(limit.orElse(10) - 1)
                    .collect(Collectors.toList());

            //return movieDAO.findByPage(start, limit.get());
        }
    }

    public MovieDetail getApiDetails(Integer tmdbId){
        RestTemplate restTemplate = new RestTemplate();
        String host = "https://api.themoviedb.org/3/movie/";
        String apiKey = "?api_key=16a34aee6285afd1a01796ab3a6a45bb&language=en-US";
        String uri = host + tmdbId.toString() + apiKey;
        return restTemplate.getForObject(uri, MovieDetail.class);
    }

   public List<Response> getData(Optional<String> sort, Optional<String> genres, Optional<Integer> limit, Optional<Integer> page, Optional<String> title){
        List<Response> responseDTOList = new ArrayList<>();
        List<MovieDTO> movieDTOList = this.getMovies(sort, genres, limit, page, title);

        for(MovieDTO m : movieDTOList){
            Optional<Integer> tmdbId = linkDAO.findTmdbId(m.getMovieId());
            if(tmdbId.isPresent()){
                MovieDetail movieDetail = getApiDetails(tmdbId.get());
                String poster_path = "https://image.tmdb.org/t/p/w500" + movieDetail.getPoster_path();
                Response response = new Response(m.getMovieId(), m.getTitle(), m.getGenres(),
                        poster_path, movieDetail.getRelease_date(), movieDetail.getBudget());
                responseDTOList.add(response);
            }
        }
        return responseDTOList;
    }

}
