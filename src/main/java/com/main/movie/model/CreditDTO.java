package com.main.movie.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CreditDTO {
    private Integer id;
    private List<CastDTO> cast;
}
