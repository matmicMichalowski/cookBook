package com.matmic.cookbook.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "authority_name")
@Getter
@Setter
@NoArgsConstructor
public class Authority implements Serializable{

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
