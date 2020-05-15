package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MovieDAO implements MovieDAOInterface{

    @Autowired
    private MoviesRepository moviesRepository;

    @Override
    public boolean save(MovieDTO movieDTO) {
        moviesRepository.save(movieDTO);
        return false;
    }

    @Override
    public List<MovieDTO> findAll() {
        return (List<MovieDTO>) moviesRepository.findAll();
    }

    @Override
    public Optional<MovieDTO> findById(Integer id) {
        return moviesRepository.findById(id);
    }

    @Override
    public Iterable<MovieDTO> findAllOrderByTitle(){
        return moviesRepository.findAllOrderByTitle();
    }

    @Override
    public List<MovieDTO> findByPage(Integer start, Integer limit){
        return (List<MovieDTO>) moviesRepository.findByPage(start, limit);
    }
}
