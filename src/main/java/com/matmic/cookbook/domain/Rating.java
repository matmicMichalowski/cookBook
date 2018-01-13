package com.matmic.cookbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "rating")
@Getter
@Setter
@NoArgsConstructor
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int evaluationSum;

    private double totalRating;

    @OneToMany
    private Set<Evaluation> usersEvaluations = new HashSet<>();

//    @OneToOne
//    private Recipe recipe;

}
