package com.main.movie.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private Integer movieId;
    @ManyToOne
    private MovieDTO movie;
    private Float rating;
    private String timestamp;

    public RatingByYear getRatingByYear(){
        Date date = new Date(Long.parseLong(this.getTimestamp()) * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String formattedDate = dateFormat.format(date);
        return new RatingByYear(Integer.valueOf(formattedDate),this.getRating());
    }
}
