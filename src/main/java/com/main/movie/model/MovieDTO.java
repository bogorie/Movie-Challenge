package com.main.movie.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
@Entity(name="movie")
public class MovieDTO {
    @Id
    @Column(name="movie_id")
    private Integer movieId;
    private String title;
    private String genres;

    @OneToMany(mappedBy="movie")
    private Set<RatingDTO> ratings;

    public void removeQuoteFromTitleIfExist(){
        if(this.title.charAt(0)=='\'' || this.title.charAt(0)=='\"'){
            this.title = this.title.replaceFirst("\'", "");
            this.title = this.title.replaceFirst("\"", "");
        }
    }

}
