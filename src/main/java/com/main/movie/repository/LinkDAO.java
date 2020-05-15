package com.main.movie.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LinkDAO implements LinkDAOInterface{
    @Autowired
    private LinkRepository linkRepository;

    @Override
    public Optional<Integer> findTmdbId(Integer movie_id){
        return linkRepository.findTmdbId(movie_id);
    }
}
