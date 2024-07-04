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
@Table(name = "trailers", indexes = {
        @Index(name = "movie_id_index", columnList = "movie_id"),
        @Index(name = "tmdb_trailer_id_index", columnList = "tmdb_trailer_id")
})
public class Trailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int trailerId;

    @Column(name = "movie_id")
    private int movieId;

    @Column(name = "key_value", length = 255)
    private String key;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "site", length = 255)
    private String site;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "iso_639_1", length = 10)
    private String iso6391;

    @Column(name = "iso_3166_1", length = 10)
    private String iso31661;

    @Column(name = "size")
    private int size;

    @Column(name = "official")
    private boolean official;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "tmdb_trailer_id", length = 50)
    private String tmdbId;
}
