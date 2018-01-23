package com.matmic.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(unique = true, nullable = false)
    private String name;

    @Email
    @NotNull
    @Size(min = 5, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    private boolean isActive = false;

    @JsonIgnore
    private String activationToken;

    @JsonIgnore
    private String resetToken;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Recipe> recipes = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_comments", joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private Set<Comment> comments = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Evaluation> evaluations = new HashSet<>();


    @ManyToMany
    @JoinTable(name= "user_authority", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "authority_name" ,referencedColumnName = "name")})
    @JsonBackReference
    private Set<Authority> authorities = new HashSet<>();
}
