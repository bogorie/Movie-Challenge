package com.main.movie.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CastDTO {
    private Integer cast_id;
    private String character;
    private String name;
    private String profile_path;
}
