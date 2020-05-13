package com.main.movie.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
@Entity(name="link")
public class LinkDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private MovieDTO movie;

    @Column(name="imdb_id")
    private BigInteger imdbId;

    @Column(name="tmdb_id")
    private BigInteger tmdbId;
}
