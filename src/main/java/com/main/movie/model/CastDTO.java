package com.main.movie.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CastDTO {
    private Integer cast_id;
    private String character;
    private String name;
    private String profile_path;
}
