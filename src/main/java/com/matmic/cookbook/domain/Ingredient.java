package com.matmic.cookbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private UnitOfMeasure unitOfMeasure;

    private int amount;

    @ManyToOne
    private Recipe recipe;

}
