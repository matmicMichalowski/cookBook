package com.matmic.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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

    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    private int cookTime;

    @Min(1)
    private int servings;

    private Difficulty difficulty;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Rating rating;

    @Lob
    @NotEmpty
    private String directions;


    @ManyToOne
    @JsonIgnore
    private User user;

    private String userName;

    @ManyToMany
    private Set<Category> categories = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Ingredient> ingredients = new HashSet<>();

    public Recipe (){
        this.setRatingForRecipe();
    }

    private void setRatingForRecipe(){
        Rating rating = new Rating();

        rating.setRecipe(this);
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
