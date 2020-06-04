package com.main.movie.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
public class MovieDBResponse {
    private int movieId;
    private String title;
    private String genres;
    private float averageRating;
    private float likedRating;
}
