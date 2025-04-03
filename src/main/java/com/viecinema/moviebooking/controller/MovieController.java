package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.MovieGenre;
import com.viecinema.moviebooking.service.MovieService;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/movies")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    /**
     * Movies management
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageMovies(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String director,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "title,asc") String sort) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Movie> movies = movieService.findAll(title, genre, status, pageable);
        
        model.addAttribute("movies", movies.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movies.getTotalPages());
        model.addAttribute("genres", movieService.getAllGenres());
        
        return "admin/movies";
    }
    
    /**
     * Movie create form
     */
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("genres", movieService.getAllGenres());
        return "admin/movie-form";
    }
    
    /**
     * Movie edit form
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
        
        model.addAttribute("movie", movie);
        model.addAttribute("genres", movieService.getAllGenres());
        model.addAttribute("selectedGenres", movie.getMovieGenres().stream().map(MovieGenre::getGenre).collect(Collectors.toList()));
        
        return "admin/movie-form";
    }
    
    /**
     * Save movie
     */
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveMovie(@ModelAttribute Movie movie, 
                           @RequestParam(required = false) String[] genres,
                           RedirectAttributes redirectAttributes) {
        try {
            movieService.save(movie);
            redirectAttributes.addFlashAttribute("success", 
                movie.getId() == null ? "Thêm phim thành công!" : "Cập nhật phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/movies";
    }
    
    /**
     * Delete movie
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa phim này. Có thể nó đang được sử dụng.");
        }
        return "redirect:/admin/movies";
    }
}
