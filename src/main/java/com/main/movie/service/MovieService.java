package com.main.movie.service;

import com.main.movie.model.MovieDTO;
import com.main.movie.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieService {
    @Autowired
    MoviesRepository moviesRepository;

    public MovieDTO getMovie(Integer movie_id){
        MovieDTO emptyMovie = new MovieDTO();
        Optional<MovieDTO> movie = moviesRepository.findById(movie_id);
        return movie.orElse(emptyMovie);
    }

    public List<MovieDTO> getMovies(Optional<String> sort, Optional<String> genres, Optional<Integer> limit, Optional<Integer> page, Optional<String> title){
        int start = page.orElse(0);
        if (start != 0) start += limit.orElse(10);

        if(sort.isPresent() && sort.get().equalsIgnoreCase("title")){
            return StreamSupport.stream(moviesRepository.findAll().spliterator(), false)
                    .sorted(Comparator.comparing(MovieDTO::getTitle))
                    .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                    .filter(f -> {
                        if (!genres.isPresent())
                            return false;
                        boolean flag = false;
                        String[] g = genres.get().split(",");
                        for(int i=0; i<g.length; i++)
                            flag = f.getGenres().toLowerCase().contains(g[i].toLowerCase());
                        return flag;
                    })
                    .skip(start)
                    .limit(limit.orElse(10))
                    .collect(Collectors.toList());
        }
        else{
            return StreamSupport.stream(moviesRepository.findAll().spliterator(), false)
                    .filter(t -> !title.isPresent() || t.getTitle().toLowerCase().contains(title.get().toLowerCase()))
                    .filter(f -> !genres.isPresent() || f.getGenres().toLowerCase().contains(genres.get().toLowerCase()))
                    .skip(start)
                    .limit(limit.orElse(10))
                    .collect(Collectors.toList());
        }
    }
}
