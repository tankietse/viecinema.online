package com.viecinema.moviebooking.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrailerDTO {
    private int trailerId;
    private int movieId;
    private String key;
    private String name;
    private String site;
    private String type;
    private String iso6391;
    private String iso31661;
    private int size;
    private boolean official;
    private LocalDateTime publishedAt;
    private String tmdbId;
}