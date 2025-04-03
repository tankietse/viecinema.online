package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.service.UserService;
import com.viecinema.moviebooking.service.BookingService;

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
@RequestMapping("/admin/user-management")
public class UserManagementController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * User management
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String email,
                             @RequestParam(defaultValue = "username,asc") String sort) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<User> users = userService.findAllByFilters(username, email, pageable);
        
        model.addAttribute("users", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        
        return "admin/users";
    }
    
    /**
     * User details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDetails(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getUserBookingHistory(id));
        
        return "admin/user-details";
    }
    
    /**
     * Toggle user lock status
     */
    @PostMapping("/{id}/toggle-lock")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleUserLock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
            
            user.setLocked(!user.isLocked());
            userService.saveUser(user);
            
            String status = user.isLocked() ? "khóa" : "mở khóa";
            redirectAttributes.addFlashAttribute("success", "Đã " + status + " tài khoản người dùng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/admin/user-management";
    }
}
