package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.service.BookingService;
import com.viecinema.moviebooking.service.MovieService;
import com.viecinema.moviebooking.service.ShowtimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private ShowtimeService showtimeService;
    
    /**
     * Dashboard page
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        // Thống kê doanh thu
        double currentMonthRevenue = bookingService.getCurrentMonthRevenue();
        double yearlyRevenue = bookingService.getYearlyRevenue();
        int pendingBookings = bookingService.countPendingBookings();
        
        // Tỷ lệ đặt vé
        double bookingRate = showtimeService.calculateBookingRate();
        
        // Phim thịnh hành
        List<Map<String, Object>> trendingMovies = movieService.getTrendingMovies(5);
        
        // Hoạt động gần đây
        List<Map<String, Object>> recentActivities = bookingService.getRecentActivities(10);
        
        model.addAttribute("currentMonthRevenue", currentMonthRevenue);
        model.addAttribute("yearlyRevenue", yearlyRevenue);
        model.addAttribute("pendingBookings", pendingBookings);
        model.addAttribute("bookingRate", Math.round(bookingRate));
        model.addAttribute("trendingMovies", trendingMovies);
        model.addAttribute("recentActivities", recentActivities);
        
        return "admin/dashboard";
    }
    
    /**
     * Settings page
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String settings(Model model) {
        // Add any settings-related attributes to the model
        return "admin/settings";
    }
}
