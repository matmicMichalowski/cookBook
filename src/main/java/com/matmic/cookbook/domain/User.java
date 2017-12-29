package com.matmic.cookbook.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @OneToMany
    private Set<Recipe> recipes = new HashSet<>();

    @OneToMany
    private Set<Comment> comments = new HashSet<>();

    @OneToMany
    private Set<Rating> ratings = new HashSet<>();
}
