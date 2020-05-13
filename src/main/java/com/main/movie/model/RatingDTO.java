package com.main.movie.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
@Entity(name="rating")
public class RatingDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private MovieDTO movie;

    private Float rating;
    private String timestamp;

}
