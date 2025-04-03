package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.ScreenType;
import com.viecinema.moviebooking.service.ScreenTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/screen-types")
public class ScreenTypeController {
    
    @Autowired
    private ScreenTypeService screenTypeService;
    
    /**
     * Screen Types Management
     */
    @GetMapping("")
    public String manageScreenTypes(Model model) {
        model.addAttribute("screenTypes", screenTypeService.findAll());
        model.addAttribute("screenType", new ScreenType());
        return "admin/screen-types";
    }
    
    /**
     * Create Screen Type
     */
    @PostMapping("/create")
    public String createScreenType(@ModelAttribute ScreenType screenType, RedirectAttributes redirectAttributes) {
        try {
            screenTypeService.save(screenType);
            redirectAttributes.addFlashAttribute("success", "Loại phòng chiếu đã được thêm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm loại phòng chiếu: " + e.getMessage());
        }
        return "redirect:/admin/screen-types";
    }
    
    /**
     * Update Screen Type
     */
    @PostMapping("/update")
    public String updateScreenType(@ModelAttribute ScreenType screenType, RedirectAttributes redirectAttributes) {
        try {
            // Check if screen type exists
            ScreenType existingType = screenTypeService.findById(screenType.getScreenTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Loại phòng chiếu không tồn tại"));
            
            // Update fields
            existingType.setTypeName(screenType.getTypeName());
            
            // Save updated screen type
            screenTypeService.save(existingType);
            
            redirectAttributes.addFlashAttribute("success", "Loại phòng chiếu đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật loại phòng chiếu: " + e.getMessage());
        }
        return "redirect:/admin/screen-types";
    }
    
    /**
     * Delete Screen Type
     */
    @DeleteMapping("/delete/{id}")
    public String deleteScreenType(@PathVariable Integer id) {
        try {
            screenTypeService.deleteById(id);
            return "redirect:/admin/screen-types";
        } catch (Exception e) {
            return "redirect:/admin/screen-types?error=Không thể xóa loại phòng chiếu này. Có thể nó đang được sử dụng.";
        }
    }
}
