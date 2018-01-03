package com.matmic.cookbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationDTO {

    private Long id;
    private Long userId;
    private int score;
    private Long ratingId;

}
