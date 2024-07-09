package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.model.*;
import com.viecinema.moviebooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieDynamicDataRepository movieDynamicDataRepository;

    @Autowired
    private TrailerRepository trailerRepository;

    @Autowired
//    private ShowtimeRepository showtimeRepository;
//
//    @Autowired
//    private ScreeningRepository screeningRepository;
//
//    @Autowired
//    private ScreenRepository screenRepository;
//
//    @Autowired
//    private TheaterRepository theaterRepository;
//
//    @Autowired
//    private SeatRepository seatRepository;

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> getCurrentMonthMovies() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        List<Movie> movies = movieRepository.findByReleaseDateBetween(startOfMonth, endOfMonth);
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> getMoviesFromCurrentToNextMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfNextMonth = now.plusMonths(1).withDayOfMonth(now.plusMonths(1).lengthOfMonth());
        List<Movie> movies = movieRepository.findByReleaseDateBetween(startOfMonth, endOfNextMonth);
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> getMoviesFromMonth(int startMonth, int numMonths) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withMonth(startMonth).withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(numMonths).withDayOfMonth(startDate.plusMonths(numMonths).lengthOfMonth());

        List<Movie> movies = movieRepository.findByReleaseDateBetween(startDate, endDate);
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    public List<MovieDTO> getComingSoonMovies() {
        LocalDate now = LocalDate.now();
        List<Movie> movies = movieRepository.findByReleaseDateAfter(now);
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return convertToDTO(movie);
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    public MovieDTO updateMovie(Integer id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setTitle(movieDTO.getTitle());
        movie.setOverview(movieDTO.getOverview());
        movie.setPosterPath(movieDTO.getPosterPath());
        movie.setBackdropPath(movieDTO.getBackdropPath());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setRuntime(movieDTO.getRuntimeMinutes());
        movie.setOriginalLanguage(movieDTO.getOriginalLanguage());
        Movie updatedMovie = movieRepository.save(movie);
        return convertToDTO(updatedMovie);
    }

    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
//        movieDTO.setTmdbID(movie.getTmdbID());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setOverview(movie.getOverview());
        movieDTO.setPosterPath(movie.getPosterPath());
        movieDTO.setBackdropPath(movie.getBackdropPath());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setRuntimeMinutes(movie.getRuntime());
        movieDTO.setOriginalLanguage(movie.getOriginalLanguage());

        Optional<MovieDynamicData> dynamicData = movieDynamicDataRepository.findByMovieId(movie.getId());
        if (dynamicData != null) {
            movieDTO.setVoteAverage(dynamicData.get().getVoteAverage());
            movieDTO.setVoteCount(dynamicData.get().getVoteCount());
            movieDTO.setPopularity(dynamicData.get().getPopularity());
        }

        List<Trailer> trailers = trailerRepository.findByMovieId(movie.getId());
        if (!trailers.isEmpty()) {
            movieDTO.setVideoId(trailers.get(0).getKey());
            /*
            Mot phim co the co nhieu hon mot trailer, co the phat trien them cho nguoi dung xem nhieu trailer hon.
            Nhung trong pham vi pj nay xac dinh trailer dau tien duoc tim thay làm trailer duy nhat duoc hien thi
            * */
        }


        return movieDTO;
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setOverview(movieDTO.getOverview());
        movie.setPosterPath(movieDTO.getPosterPath());
        movie.setBackdropPath(movieDTO.getBackdropPath());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setRuntime(movieDTO.getRuntimeMinutes());
        movie.setOriginalLanguage(movieDTO.getOriginalLanguage());

        return movie;
    }
}
