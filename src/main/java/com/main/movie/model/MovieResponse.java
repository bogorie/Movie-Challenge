package com.main.movie.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
public class MovieResponse {
    private int movieId;
    private String title;
    private String genres;
    private float rating;
    private String poster_path;
    private String release_date;
    private float budget;
    private String overview;
    private int runtime;
}
