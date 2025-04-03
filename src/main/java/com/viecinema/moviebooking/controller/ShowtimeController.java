package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Screening;
import com.viecinema.moviebooking.model.Showtime;
import com.viecinema.moviebooking.service.ShowtimeService;
import com.viecinema.moviebooking.service.MovieService;
import com.viecinema.moviebooking.service.TheaterService;
import com.viecinema.moviebooking.service.ScreenService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("/admin/showtimes")
public class ShowtimeController {
    
    @Autowired
    private ShowtimeService showtimeService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private TheaterService theaterService;
    
    @Autowired
    private ScreenService screenService;
    
    /**
     * Showtimes management
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageShowtimes(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) Long movieId,
                                 @RequestParam(required = false) Long theaterId,
                                 @RequestParam(required = false) LocalDate startDate,
                                 @RequestParam(required = false) LocalDate endDate) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        Page<Showtime> showtimes = showtimeService.findAllByFilters(movieId, theaterId, startDate, endDate, pageable);
        
        model.addAttribute("showtimes", showtimes.getContent());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", showtimes.getTotalPages());
        
        return "admin/showtimes";
    }
    
    /**
     * Showtime create form
     */
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createShowtimeForm(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theaters", theaterService.findAll());
        return "admin/showtime-form";
    }
    
    /**
     * Get screens by theater
     */
    @GetMapping("/screens-by-theater")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<Screen> getScreensByTheater(@RequestParam Long theaterId) {
        System.out.println("Getting screens for theater ID: " + theaterId);
        try {
            // Try direct method first
            List<Screen> directScreens = screenService.findByTheaterId(theaterService.findById(theaterId.intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Theater not found: " + theaterId)));
            
            if (directScreens != null && !directScreens.isEmpty()) {
                System.out.println("Found " + directScreens.size() + " screens using direct method");
                return directScreens;
            }
            
            // Fall back to pageable method
            Page<Screen> screenPage = screenService.findByTheater(theaterId, null);
            if (screenPage != null) {
                List<Screen> screens = screenPage.getContent();
                System.out.println("Found " + screens.size() + " screens using pageable method");
                return screens;
            } else {
                System.out.println("No screens found for theater ID: " + theaterId);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error getting screens for theater ID: " + theaterId);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Showtime edit form
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editShowtimeForm(@PathVariable Long id, Model model) {
        Showtime showtime = showtimeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid showtime ID: " + id));
        
        model.addAttribute("showtime", showtime);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theaters", theaterService.findAll());
        List<Screening> screenings = showtime.getScreenings();
        if (screenings.size() > 0) {
            Long theaterId = screenings.get(0).getScreen().getTheater().getTheaterId().longValue();
            model.addAttribute("screens", screenService.findByTheater(theaterId, null).getContent());
        }
        return "admin/showtime-form";
    }
    
    /**
     * Save showtime
     */
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveShowtime(@ModelAttribute Showtime showtime, RedirectAttributes redirectAttributes) {
        try {
            showtimeService.save(showtime);
            redirectAttributes.addFlashAttribute("success", 
                showtime.getShowtimeId() == null ? "Thêm lịch chiếu thành công!" : "Cập nhật lịch chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/showtimes";
    }
    
    /**
     * Delete showtime
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteShowtime(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            showtimeService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa lịch chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa lịch chiếu này. Có thể nó đang được sử dụng.");
        }
        return "redirect:/admin/showtimes";
    }
}
