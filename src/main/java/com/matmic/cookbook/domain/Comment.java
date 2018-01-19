package com.matmic.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
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

}
