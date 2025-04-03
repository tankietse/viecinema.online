package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movies", indexes = {
        @Index(name = "title_index", columnList = "title"),
        @Index(name = "tmdb_id_index", columnList = "tmdb_id")
})
public class Movie {
    
    public enum MovieStatus {
        COMING_SOON,
        NOW_SHOWING,
        ENDED,
        CANCELLED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Long tmdbID;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "overview", length = 1000)
    private String overview;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "backdrop_path")
    private String backdropPath;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "original_language")
    private String originalLanguage;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MovieStatus status = MovieStatus.COMING_SOON;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieGenre> movieGenres = new HashSet<>();
    
    // Convenience methods for view count
    public Integer getViewCount() {
        return viewCount != null ? viewCount : 0;
    }
    
    public void incrementViewCount() {
        if (viewCount == null) viewCount = 0;
        viewCount++;
    }
}
