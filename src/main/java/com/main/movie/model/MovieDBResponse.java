package com.main.movie.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
public class MovieDBResponse {
    private Integer movieId;
    private String title;
    private String genres;
    private Float averageRating;
}
