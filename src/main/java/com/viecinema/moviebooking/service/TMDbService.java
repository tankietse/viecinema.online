package com.viecinema.moviebooking.service;

import info.movito.themoviedbapi.model.core.video.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TMDbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Video> getMovieTrailers(int movieId) {
        String url = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=%s&language=vi-VN", movieId, apiKey);
        TmdbVideoResponse response = restTemplate.getForObject(url, TmdbVideoResponse.class);
        assert response != null;
        return response.getResults().stream()
                .filter(video -> "Trailer".equalsIgnoreCase(video.getType()) && "YouTube".equalsIgnoreCase(video.getSite()))
                .collect(Collectors.toList());
    }

    public String getPopularMovies() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=vi-VN";
        return restTemplate.getForObject(url, String.class);
    }

    public String getMovieDetails(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey + "&language=vi-VN";
        return restTemplate.getForObject(url, String.class);
    }

    public String searchMovies(String query) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query + "&language=vi-VN";
        return restTemplate.getForObject(url, String.class);
    }
}
