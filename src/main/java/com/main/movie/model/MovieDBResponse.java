package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieDBResponse {
    private Integer movieId;
    private String title;
    private String genres;
    private Float averageRating;
}
