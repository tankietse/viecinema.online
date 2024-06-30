package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="movie_dynamic_data")
public class MovieDynamicData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private double voteAverage;

    private int voteCount;

    private double popularity;

    private LocalDateTime lastUpdated;

    public MovieDynamicData(Movie movie) {
        this.movie = movie;
    }
}
