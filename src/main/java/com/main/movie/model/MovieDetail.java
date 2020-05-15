package com.main.movie.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
@Setter
public class MovieDetail {
    private Integer id;
    private String poster_path;
    private String release_date;
    private BigInteger budget;
}
