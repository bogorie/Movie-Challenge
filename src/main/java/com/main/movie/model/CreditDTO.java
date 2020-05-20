package com.main.movie.model;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreditDTO {
    private Integer id;
    private List<CastDTO> cast;
}
