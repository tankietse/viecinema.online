package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("movies", movieService.getMoviesFromCurrentToNextMonth());
        return "home";
    }
    @GetMapping("/now-playing")
    public String nowplaying (Model model) {
        model.addAttribute("nowPlayingMovies", movieService.getCurrentMonthMovies());
        return "now-playing";
    }

    @GetMapping("/coming-soon")
    public String comingSoon(Model model) {
        model.addAttribute("comingSoonMovies", movieService.getComingSoonMovies());
        return "coming-soon";
    }
}