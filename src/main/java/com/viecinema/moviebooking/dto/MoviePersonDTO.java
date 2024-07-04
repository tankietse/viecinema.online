package com.viecinema.moviebooking.dto;

import lombok.Data;

@Data
public class MoviePersonDTO {
    private int id;
    private int movieId;
    private int personId;
    private String role;
}
