package com.viecinema.moviebooking.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.movito.themoviedbapi.model.core.video.Video;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbVideoResponse {

    @JsonProperty("results")
    private List<Video> results;

    // Getters and setters
    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
