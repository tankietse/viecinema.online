package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "movie_persons")
public class MoviePerson {
    public enum Role {
        ACTOR,
        DIRECTOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Enumerated(EnumType.STRING)
    private Role role;
}

