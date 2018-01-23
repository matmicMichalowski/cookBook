package com.matmic.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Size(min = 5, max = 500)
    private String comment;


    @Size(min = 1, max = 50)
    private String userName;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private User user;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Recipe recipe;

    public void setUser(User user){

        if(sameAsFormerUser(user)){
            return;
        }

        User actualUser = this.user;
        this.user = user;

        if(actualUser != null){
            actualUser.getComments().remove(this);
        }
        if(user != null){
            user.getComments().add(this);
        }
    }

    private boolean sameAsFormerUser(User newUser){
        return user == null ? newUser == null : user.equals(newUser);
    }

    public void setRecipe(Recipe recipe){

        if(sameAsFormerRecipe(recipe)){
            return;
        }

        Recipe actualRecipe = this.recipe;
        this.recipe = recipe;

        if(actualRecipe != null){
            actualRecipe.getComments().remove(this);
        }
        if(recipe != null){
            recipe.getComments().add(this);
        }
    }

    private boolean sameAsFormerRecipe(Recipe newRecipe){
        return recipe == null ? newRecipe == null : recipe.equals(newRecipe);
    }

}
