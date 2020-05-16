package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Response {
    private int movieId;
    private String title;
    private String genres;
    private float rating;
    private String poster_path;
    private String release_date;
    private float budget;
    private String overview;
}
