package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.model.*;
import com.viecinema.moviebooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
    private GenreRepository genreRepository;

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

    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return convertToDTO(movie);
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
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

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
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

    /**
     * Tìm tất cả phim với phân trang và filter
     */
    public Page<Movie> findAll(String title, String genreId, String status, Pageable pageable) {
        // Create a custom implementation since some repository methods are not available
        List<Movie> allMovies = movieRepository.findAll();
        List<Movie> filteredMovies = allMovies.stream()
            .filter(movie -> {
                boolean matches = true;
                
                // Filter by title if provided
                if (title != null && !title.isEmpty()) {
                    matches = matches && movie.getTitle().toLowerCase().contains(title.toLowerCase());
                }
                
                // Filter by genre if provided
                if (genreId != null && !genreId.isEmpty()) {
                    try {
                        final Integer genreIdValue = Integer.parseInt(genreId);
                        matches = matches && movie.getMovieGenres().stream()
                            .anyMatch(mg -> mg.getGenre().getGenreId().equals(genreIdValue));
                    } catch (NumberFormatException e) {
                        // If genreId is not a number, try to match by genre name
                        final String genreName = genreId.toLowerCase();
                        matches = matches && movie.getMovieGenres().stream()
                            .anyMatch(mg -> mg.getGenre().getName().toLowerCase().contains(genreName));
                    }
                }
                
                // Filter by status if provided
                if (status != null && !status.isEmpty()) {
                    try {
                        Movie.MovieStatus statusEnum = Movie.MovieStatus.valueOf(status);
                        matches = matches && movie.getStatus() == statusEnum;
                    } catch (IllegalArgumentException e) {
                        // Invalid status value, ignore this filter
                    }
                }
                
                return matches;
            })
            .collect(Collectors.toList());
        
        // Apply sorting
        if (pageable.getSort().isSorted()) {
            // Implement sorting logic here if needed
        }
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredMovies.size());
        
        if (start > filteredMovies.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredMovies.size());
        }
        
        return new PageImpl<>(
            filteredMovies.subList(start, end),
            pageable,
            filteredMovies.size()
        );
    }

    /**
     * Lấy danh sách phim đang hoạt động (COMING_SOON hoặc NOW_SHOWING)
     */
    public List<Movie> findAllActive() {
        return movieRepository.findAll().stream()
            .filter(movie -> movie.getStatus() == Movie.MovieStatus.COMING_SOON || 
                   movie.getStatus() == Movie.MovieStatus.NOW_SHOWING)
            .collect(Collectors.toList());
    }

    /**
     * Tìm phim theo ID
     */
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    /**
     * Lấy danh sách thể loại phim
     */
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /**
     * Lưu hoặc cập nhật phim
     */
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    /**
     * Xóa phim
     */
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    /**
     * Lấy danh sách phim thịnh hành dựa trên doanh thu hoặc lượt xem
     */
    public List<Map<String, Object>> getTrendingMovies(int limit) {
        List<Movie> movies = movieRepository.findAll().stream()
            .sorted((m1, m2) -> m2.getViewCount().compareTo(m1.getViewCount()))
            .limit(10)
            .collect(Collectors.toList());
            
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (int i = 0; i < Math.min(limit, movies.size()); i++) {
            Movie movie = movies.get(i);
            Map<String, Object> movieMap = new HashMap<>();
            movieMap.put("title", movie.getTitle());
            movieMap.put("ticketsSold", movie.getViewCount());
            movieMap.put("revenue", movie.getViewCount() * 85000); // Giả định giá vé trung bình là 85,000đ
            movieMap.put("rating", (new Random().nextInt(3) + 3)); // Giả định rating 3-5 sao
            result.add(movieMap);
        }
        
        return result;
    }
}
