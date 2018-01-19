package com.matmic.cookbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Min(1)
    @Max(5)
    private int score;

    @ManyToOne
    private Rating rating;

    public void setUser(User user){

        if (sameAsFormerUser(user)){
            return;
        }

        User actualUser = this.user;
        this.user = user;

        if (actualUser != null){
            actualUser.getEvaluations().remove(this);
        }
        if (user != null){
            user.getEvaluations().add(this);
        }
    }

    private boolean sameAsFormerUser(User newUser){
        return user == null ? newUser == null : user.equals(newUser);
    }

    public void setRating(Rating rating){

        if (sameAsFormerRating(rating)){
            return;
        }

        Rating actualRating = this.rating;
        this.rating = rating;

        if(actualRating != null){
            actualRating.getUsersEvaluations().remove(this);
        }
        if (rating != null){
            rating.getUsersEvaluations().add(this);
        }
    }

    private boolean sameAsFormerRating(Rating newRating){
        return rating == null ? newRating == null : rating.equals(newRating);
    }
}
