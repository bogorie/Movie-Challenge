package com.main.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@AllArgsConstructor
@Getter
@Setter
public class Response {
    private Integer movieId;
    private String title;
    private String genres;
    private String poster_path;
    private String release_date;
    private BigInteger budget;
}
