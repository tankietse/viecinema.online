package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.SeatType;
import com.viecinema.moviebooking.service.SeatTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/seat-types")
public class SeatTypeController {
    
    @Autowired
    private SeatTypeService seatTypeService;
    
    /**
     * Seat Types Management
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageSeatTypes(Model model) {
        model.addAttribute("seatTypes", seatTypeService.findAll());
        model.addAttribute("seatType", new SeatType());
        return "admin/seat-types";
    }
    
    /**
     * Create Seat Type
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createSeatType(@ModelAttribute SeatType seatType, RedirectAttributes redirectAttributes) {
        try {
            seatTypeService.save(seatType);
            redirectAttributes.addFlashAttribute("success", "Loại ghế đã được thêm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm loại ghế: " + e.getMessage());
        }
        return "redirect:/admin/seat-types";
    }
    
    /**
     * Update Seat Type
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateSeatType(@ModelAttribute SeatType seatType, RedirectAttributes redirectAttributes) {
        try {
            // Check if seat type exists
            SeatType existingType = seatTypeService.findById(seatType.getSeatTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Loại ghế không tồn tại"));
            
            // Update fields
            existingType.setTypeName(seatType.getTypeName());
            existingType.setSeatPrice(seatType.getSeatPrice());
            
            // Save updated seat type
            seatTypeService.save(existingType);
            
            redirectAttributes.addFlashAttribute("success", "Loại ghế đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật loại ghế: " + e.getMessage());
        }
        return "redirect:/admin/seat-types";
    }
    
    /**
     * Delete Seat Type
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSeatType(@PathVariable Integer id) {
        try {
            seatTypeService.deleteById(id);
            return "redirect:/admin/seat-types";
        } catch (Exception e) {
            return "redirect:/admin/seat-types?error=Không thể xóa loại ghế này. Có thể nó đang được sử dụng.";
        }
    }
}
