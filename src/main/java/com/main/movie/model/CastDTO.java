package com.main.movie.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CastDTO {
    private Integer cast_id;
    private String character;
    private String name;
    private String profile_path;
}
