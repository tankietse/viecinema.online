package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieControler {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movie-detail")
    public String getMovieDetailPage(@RequestParam("id") Integer id, Model model) {
        MovieDTO movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "movie/movie-detail";
    }
}
