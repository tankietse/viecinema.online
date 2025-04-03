package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.service.TheaterService;
import com.viecinema.moviebooking.service.ScreenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/theaters")
public class TheaterController {
    
    @Autowired
    private TheaterService theaterService;
    
    @Autowired
    private ScreenService screenService;
    
    /**
     * Theater management
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageTheaters(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String location,
                               @RequestParam(defaultValue = "name,asc") String sort) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        List<Theater> theaters = theaterService.findAllByFilters(name, location, pageable);
        
        model.addAttribute("theaters", theaters);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, (int) Math.ceil(theaterService.count() * 1.0 / size)));
        
        return "admin/theaters";
    }
    
    /**
     * View theater screens
     */
    @GetMapping("/{id}/screens")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewTheaterScreens(@PathVariable("id") Integer id, Model model) {
        Theater theater = theaterService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid theater ID: " + id));
        
        List<Screen> screens = screenService.findByTheater(theater);
        
        model.addAttribute("theater", theater);
        model.addAttribute("screens", screens);
        
        return "admin/theater-screens";
    }
    
    /**
     * Create theater form
     */
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createTheaterForm(Model model) {
        model.addAttribute("theater", new Theater());
        return "admin/theater-form";
    }
    
    /**
     * Edit theater form
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editTheaterForm(@PathVariable Integer id, Model model) {
        Theater theater = theaterService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid theater ID: " + id));
        
        model.addAttribute("theater", theater);
        return "admin/theater-form";
    }
    
    /**
     * Save theater
     */
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveTheater(@ModelAttribute Theater theater, RedirectAttributes redirectAttributes) {
        try {
            theaterService.save(theater);
            redirectAttributes.addFlashAttribute("success", 
                theater.getTheaterId() == null ? "Thêm rạp chiếu thành công!" : "Cập nhật rạp chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/theaters";
    }
    
    /**
     * Delete theater
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTheater(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            theaterService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa rạp chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa rạp chiếu này. Có thể nó đang được sử dụng.");
        }
        return "redirect:/admin/theaters";
    }
}
