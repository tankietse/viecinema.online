package com.viecinema.moviebooking.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MovieDTO {
    private Long id;
    private Long tmdbID;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;
    private int runtimeMinutes;
    private String originalLanguage;
    private double voteAverage;
    private int voteCount;
    private double popularity;
    private boolean adult;
    private String status;
    private int budget;
    private int revenue;
    private String videoId;
    private List<Map<String, Object>> genres;
    private List<Map<String, Object>> productionCompanies;
    private List<Map<String, Object>> productionCountries;
    private List<Map<String, Object>> spokenLanguages;
}
