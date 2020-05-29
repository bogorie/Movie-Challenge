package com.main.movie.model;

import lombok.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(includeFieldNames=true)
public class GenreResponse {
    private Set<String> genres;
}
