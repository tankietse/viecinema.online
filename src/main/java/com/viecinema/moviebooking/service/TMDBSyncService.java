package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.dto.TrailerDTO;
import com.viecinema.moviebooking.model.*;
import com.viecinema.moviebooking.repository.*;
import com.viecinema.moviebooking.util.TMDBApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TMDBSyncService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieDynamicDataRepository movieDynamicDataRepository;
    private final TMDBApiClient tmdbApiClient;
    @Autowired
    private TrailerRepository trailerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    public TMDBSyncService(MovieRepository movieRepository,
                           MovieDynamicDataRepository movieDynamicDataRepository,
                           TMDBApiClient tmdbApiClient, TrailerRepository trailerRepository) {
        this.movieRepository = movieRepository;
        this.movieDynamicDataRepository = movieDynamicDataRepository;
        this.tmdbApiClient = tmdbApiClient;
        this.trailerRepository = trailerRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Chạy hàng ngày lúc 0 giờ sáng
    public void syncPopularMovies() {
        for (int page = 1; page <= 30; page++) {
            List<MovieDTO> popularMovies = tmdbApiClient.getPopularMovies(page);
            for (MovieDTO movieDTO : popularMovies) {
                updateOrInsertMovie(movieDTO.getId());
            }
        }
        System.out.println("Sync completed !!");
    }

    private void updateOrInsertMovie(int tmdbId) {
        MovieDTO movieDTO = tmdbApiClient.getMovieDetails(tmdbId);

        Movie movie = movieRepository.findByTmdbID(tmdbId)
                .orElse(new Movie());

        // Cập nhật thông tin cơ bản của phim
        movie.setTmdbID(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setOverview(movieDTO.getOverview());
        movie.setPosterPath(movieDTO.getPosterPath());
        movie.setBackdropPath(movieDTO.getBackdropPath());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setRuntime(movieDTO.getRuntimeMinutes());
        movie.setOriginalLanguage(movieDTO.getOriginalLanguage());
        // ... cập nhật các trường khác nếu có

        movieRepository.save(movie);

        // Cập nhật dữ liệu "động" (Dữ liệu được cập nhật thường xuyên)
        MovieDynamicData dynamicData = movieDynamicDataRepository.findByMovieId(movie.getId())
                .orElse(new MovieDynamicData(movie));
        dynamicData.setVoteAverage(movieDTO.getVoteAverage());
        dynamicData.setVoteCount(movieDTO.getVoteCount());
        dynamicData.setPopularity(movieDTO.getPopularity());
        dynamicData.setLastUpdated(LocalDateTime.now());
        movieDynamicDataRepository.save(dynamicData);

        // Cập nhật thông tin các trailer (Một phim có thể có nhiều trailer hoặc không có trailer nào)
        List<TrailerDTO> trailers = tmdbApiClient.getMovieTrailers(movieDTO.getId());
        for (TrailerDTO trailerDTO : trailers) {
            Trailer trailer = new Trailer();
            trailer.setMovieId(movie.getId());
            trailer.setTmdbId(trailerDTO.getTmdbId());
            trailer.setIso6391(trailerDTO.getIso6391());
            trailer.setIso31661(trailerDTO.getIso31661());
            trailer.setKey(trailerDTO.getKey());
            trailer.setName(trailerDTO.getName());
            trailer.setSite(trailerDTO.getSite());
            trailer.setSize(trailerDTO.getSize());
            trailer.setType(trailerDTO.getType());
            trailer.setOfficial(trailerDTO.isOfficial());
            trailer.setPublishedAt(trailerDTO.getPublishedAt());
            trailerRepository.save(trailer);
        }
    }
}
