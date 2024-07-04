package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "movie_genres")
@Data
public class MovieGenre {
    @EmbeddedId
    private MovieGenreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;
}

