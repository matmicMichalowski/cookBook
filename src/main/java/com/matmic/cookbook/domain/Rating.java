package com.matmic.cookbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int evaluationSum;

    private double totalRating;

    @OneToMany
    private Set<Evaluation> usersEvaluations = new HashSet<>();

    @OneToOne
    private Recipe recipe;

}
