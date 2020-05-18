package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieResponse {
    private int movieId;
    private String title;
    private String genres;
    private float rating;
    private String poster_path;
    private String release_date;
    private float budget;
    private String overview;
}
