package com.main.movie.service;

import com.main.movie.model.MovieDTO;
import com.main.movie.repository.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieDAO movieDAO;

    public MovieDTO getMovie(Integer movie_id){
        MovieDTO emptyMovie = new MovieDTO();
        Optional<MovieDTO> movie = movieDAO.findById(movie_id);
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
}
