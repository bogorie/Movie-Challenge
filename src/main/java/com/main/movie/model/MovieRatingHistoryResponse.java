package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieRatingHistoryResponse {
    private Integer movieId;
    private List<RatingByYear> ratings;
}
