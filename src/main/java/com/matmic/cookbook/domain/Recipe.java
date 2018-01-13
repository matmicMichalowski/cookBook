package com.matmic.cookbook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "recipe")
@Getter
@Setter
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int cookTime;
    private int servings;
    private Difficulty difficulty;

//    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Rating rating;

    @Lob
    private String directions;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Category> categories = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients = new HashSet<>();

    public Recipe (){
        this.setRatingForRecipe();
    }

    private void setRatingForRecipe(){
        Rating rating = new Rating();
        this.setRating(rating);
    }

    public void setUser(User user){

        if(sameAsFormerUser(user)){
            return;
        }

        User actualUser = this.user;
        this.user = user;

        if (actualUser != null){
            actualUser.getRecipes().remove(this);
        }
        if (user != null){
            user.getRecipes().add(this);
        }
    }

    private boolean sameAsFormerUser(User newUser){
        return this.user == null ? newUser == null : this.user.equals(newUser);
    }


}
