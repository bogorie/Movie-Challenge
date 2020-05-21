package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieDetail {
    private int id;
    private String poster_path;
    private String release_date;
    private float budget;
    private String overview;
    private float vote_average;
}
