package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TheaterService theaterService;
    @GetMapping("/admin")
    public String adminDashboard(){
        return "admin/dashboard";
    }
}
