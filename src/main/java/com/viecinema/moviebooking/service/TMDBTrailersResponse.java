package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.TrailerDTO;
import lombok.Data;

import java.util.List;

@Data
public class TMDBTrailersResponse {
    private List<TrailerDTO> results;
}
