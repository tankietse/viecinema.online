package com.viecinema.moviebooking.dto;

import lombok.Data;

@Data
public class MovieGenreDTO {
    private Long movieId;
    private Long genreId;
    private String movieTitle;
    private String genreName;
}