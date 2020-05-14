package com.main.movie.repository;

import com.main.movie.model.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

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
        return (List) moviesRepository.findAll();
    }

    @Override
    public Optional<MovieDTO> findById(Integer id) {
        return moviesRepository.findById(id);
    }

    @Override
    public Iterable<MovieDTO> findAllOrderByTitle(){
        return moviesRepository.findAllOrderByTitle();
    }
}
