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

    private double value;

    @OneToMany
    private Set<User> usersRatings = new HashSet<>();

    @OneToOne
    private Recipe recipe;

}
