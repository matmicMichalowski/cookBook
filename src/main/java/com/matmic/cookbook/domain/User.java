package com.matmic.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Recipe> recipes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();
}
