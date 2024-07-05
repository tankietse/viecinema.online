package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.service.TheaterService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @GetMapping
    public String showTheaterList(@NotNull Model model) {
        List<Theater> theaters = theaterService.getAllTheaters();
        model.addAttribute("theaters", theaters);
        return "admin/manage_theaters";
    }

    @PostMapping("/create")
    public String createTheater(@Valid Theater theater, @NotNull BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/manage_theaters";
        }
        theaterService.saveTheater(theater);
        return "redirect:/admin/theaters";
    }

    @PostMapping("/edit/{id}")
    public String editTheater(@PathVariable("id") Integer id, @Valid Theater theater, @NotNull BindingResult result, Model model) {
        if (result.hasErrors()) {
            theater.setTheaterId(id);
            return "admin/manage_theaters";
        }
        theater.setTheaterId(id);
        theaterService.saveTheater(theater);
        return "redirect:/admin/theaters";
    }

    @PostMapping("/delete/{id}")
    public String deleteTheater(@PathVariable("id") Integer id, Model model) {
        theaterService.deleteTheater(id);
        return "redirect:/admin/theaters";
    }
}
