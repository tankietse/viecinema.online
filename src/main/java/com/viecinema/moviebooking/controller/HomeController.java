package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/home")
    public String home(Model model) {
        LocalDate now = LocalDate.now();
        int startMonth = now.minusMonths(1).getMonthValue();
        int numMonths = 3; //mac dinh 3 thang

        model.addAttribute("movies", movieService.getMoviesFromMonth(startMonth, numMonths));
        return "movie/home";
    }

    @GetMapping("/now-playing")
    public String nowplaying (Model model) {
        model.addAttribute("nowPlayingMovies", movieService.getCurrentMonthMovies());
        return "movie/now-playing";
    }

    @GetMapping("/coming-soon")
    public String comingSoon(Model model) {
        model.addAttribute("comingSoonMovies", movieService.getComingSoonMovies());
        return "movie/coming-soon";
    }
}