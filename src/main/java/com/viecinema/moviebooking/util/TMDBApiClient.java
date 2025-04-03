package com.viecinema.moviebooking.util;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.dto.TrailerDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TMDBApiClient {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.access.token}")
    private String accessToken;

    public TMDBApiClient() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<MovieDTO> getPopularMovies(int page) {
        String url = "https://api.themoviedb.org/3/movie/popular?language=vi-VN&api_key=" + apiKey + "&page=" + page;
        return fetchMovies(url);
    }

    public List<MovieDTO> getTopRatedMovies() {
        return fetchMovies("https://api.themoviedb.org/3/movie/top_rated?language=vi-VN&api_key=" + apiKey);
    }

    public List<MovieDTO> getUpcomingMovies() {
        return fetchMovies("https://api.themoviedb.org/3/movie/upcoming?language=vi-VN&api_key=" + apiKey);
    }

    public List<MovieDTO> getNowPlayingMovies() {
        return fetchMovies("https://api.themoviedb.org/3/movie/now_playing?language=vi-VN&api_key=" + apiKey);
    }

    public List<TrailerDTO> getMovieTrailers(Long tmdbId) {
        String url = String.format("https://api.themoviedb.org/3/movie/%d/videos?language=vi-VN&api_key=%s", tmdbId, apiKey);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> results = objectMapper.convertValue(responseMap.get("results"), new TypeReference<List<Map<String, Object>>>() {});

            return results.stream().map(this::mapToTrailerDTO).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch movie trailers from TMDB", e);
        }
    }

    private TrailerDTO mapToTrailerDTO(Map<String, Object> trailerData) {
        TrailerDTO trailerDTO = new TrailerDTO();
        trailerDTO.setIso6391((String) trailerData.get("iso_639_1"));
        trailerDTO.setIso31661((String) trailerData.get("iso_3166_1"));
        trailerDTO.setName((String) trailerData.get("name"));
        trailerDTO.setKey((String) trailerData.get("key"));
        trailerDTO.setSite((String) trailerData.get("site"));
        trailerDTO.setSize(trailerData.containsKey("size") && trailerData.get("size") != null ? (int) trailerData.get("size") : 0);
        trailerDTO.setType((String) trailerData.get("type"));
        trailerDTO.setOfficial(Optional.ofNullable(trailerData.get("official"))
                .map(Boolean.class::cast)
                .orElse(false));
        trailerDTO.setPublishedAt(Optional.ofNullable((String) trailerData.get("published_at"))
                .map(OffsetDateTime::parse)
                .map(OffsetDateTime::toLocalDateTime)
                .orElse(null));

        trailerDTO.setTmdbId(String.valueOf(trailerData.get("id")));
        return trailerDTO;
    }


    private List<MovieDTO> fetchMovies(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> results = objectMapper.convertValue(responseMap.get("results"), new TypeReference<List<Map<String, Object>>>() {});

            return results.stream().map(this::fetchAndMapToMovieDTO).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch movies from TMDB", e);
        }
    }

    private MovieDTO fetchAndMapToMovieDTO(Map<String, Object> movieData) {
        return getMovieDetails(((Number) movieData.get("id")).longValue());
    }

    public MovieDTO getMovieDetails(Long movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?language=vi-VN";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Map<String, Object> movieData = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            return mapToMovieDTO(movieData);

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch movie details from TMDB", e);
        }
    }


    private MovieDTO mapToMovieDTO(Map<String, Object> movieData) {
        try {
            Long id = ((Number) movieData.get("id")).longValue();
            String title = (String) movieData.get("title");
            String overview = (String) movieData.get("overview");
            String posterPath = (String) movieData.get("poster_path");
            String backdropPath = (String) movieData.get("backdrop_path");
            String releaseDateStr = (String) movieData.get("release_date");
            LocalDate localDate = null;
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateStr);
                localDate = releaseDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            }
            String originalLanguage = (String) movieData.get("original_language");
            double voteAverage = ((Number) movieData.get("vote_average")).doubleValue();
            int voteCount = (int) movieData.get("vote_count");
            double popularity = ((Number) movieData.get("popularity")).doubleValue();
            int runtime = movieData.containsKey("runtime") ? ((Number) movieData.get("runtime")).intValue() : 0;

            MovieDTO dto = new MovieDTO();
            dto.setId(id);
            dto.setTitle(title);
            dto.setOverview(overview);
            dto.setPosterPath(posterPath);
            dto.setBackdropPath(backdropPath);
            dto.setReleaseDate(localDate);
            dto.setRuntimeMinutes(runtime);
            dto.setOriginalLanguage(originalLanguage);
            dto.setVoteAverage(voteAverage);
            dto.setVoteCount(voteCount);
            dto.setPopularity(popularity);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to map movie data to MovieDTO", e);
        }
    }

}
