package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// Định nghĩa khóa chính tổng hợp (@Embeddable)
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieGenreId implements Serializable {
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "genre_id")
    private Integer genreId;
}
