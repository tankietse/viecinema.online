package com.viecinema.moviebooking.dto;

import lombok.Data;

@Data
public class ScreenDTO {
    private int screenId;
    private int theaterId;
    private String name;
    private int capacity;
    private int screenTypeId;
}
