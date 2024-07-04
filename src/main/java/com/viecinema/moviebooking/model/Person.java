package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "persons", indexes = {
        @Index(name = "name_index", columnList = "name"),
        @Index(name = "date_of_birth_index", columnList = "date_of_birth")
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int personId;

    private String name;
    private String bio;
    private Date dateOfBirth;
    private String nationality;
}
