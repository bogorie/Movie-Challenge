package com.main.movie.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class CreditDTO {
    private Integer id;
    private List<CastDTO> cast;
}
