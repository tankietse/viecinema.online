package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.ScreenType;
import com.viecinema.moviebooking.model.Seat;
import com.viecinema.moviebooking.model.SeatType;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.service.ScreenService;
import com.viecinema.moviebooking.service.ScreenTypeService;
import com.viecinema.moviebooking.service.SeatService;
import com.viecinema.moviebooking.service.SeatTypeService;
import com.viecinema.moviebooking.service.TheaterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/screens")
public class ScreenController {
    
    @Autowired
    private ScreenService screenService;
    
    @Autowired
    private TheaterService theaterService;
    
    @Autowired
    private ScreenTypeService screenTypeService;
    
    @Autowired
    private SeatTypeService seatTypeService;
    
    @Autowired
    private SeatService seatService;
    
    /**
     * Screens Management
     */
    @GetMapping("")
    public String manageScreens(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Long theaterId,
                              @RequestParam(required = false) Integer screenTypeId) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Screen> screens;
        
        if (theaterId != null && screenTypeId != null) {
            screens = screenService.findByTheaterAndScreenType(theaterId, screenTypeId, pageable);
        } else if (theaterId != null) {
            screens = screenService.findByTheater(theaterId, pageable);
        } else if (screenTypeId != null) {
            screens = screenService.findByScreenType(screenTypeId, pageable);
        } else {
            screens = screenService.findAll(pageable);
        }
        
        model.addAttribute("screens", screens.getContent());
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("screenTypes", screenTypeService.findAll());
        model.addAttribute("seatTypes", seatTypeService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", screens.getTotalPages());
        model.addAttribute("size", size);
        
        return "admin/screens";
    }
    
    /**
     * Create Screen
     */
    @PostMapping("/create")
    public String createScreen(@RequestParam("name") String name,
                             @RequestParam("theaterId") Long theaterId,
                             @RequestParam("screenTypeId") Integer screenTypeId,
                             @RequestParam("capacity") Integer capacity,
                             @RequestParam("rowNumber") Integer rowNumber,
                             @RequestParam("seatNumber") Integer seatNumber,
                             @RequestParam("seatTypeId") Integer seatTypeId,
                             RedirectAttributes redirectAttributes) {
        try {
            // Get the theater and screen type entities
            Theater theater = theaterService.findById(theaterId.intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
            
            ScreenType screenType = screenTypeService.findById(screenTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("Screen type not found"));
            
            SeatType seatType = seatTypeService.findById(seatTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("Seat type not found"));
            
            // Create a new seat
            Seat seat = new Seat();
            seat.setRowNumber(rowNumber);
            seat.setSeatNumber(seatNumber);
            seat.setSeatType(seatType);
            
            // Save the seat
            seat = seatService.save(seat);
            
            // Create a new screen
            Screen screen = new Screen();
            screen.setName(name);
            screen.setTheater(theater);
            screen.setScreenType(screenType);
            screen.setCapacity(capacity);
            screen.setSeat(seat);
            
            // Save the screen
            screenService.save(screen);
            
            redirectAttributes.addFlashAttribute("success", "Phòng chiếu đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo phòng chiếu: " + e.getMessage());
        }
        
        return "redirect:/admin/screens";
    }
    
    /**
     * Screen create form
     */
    @GetMapping("/create")
    public String createScreenForm(Model model) {
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("screenTypes", screenTypeService.findAll());
        model.addAttribute("seatTypes", seatTypeService.findAll());
        return "admin/screen-form";
    }
    
    /**
     * Screen edit form
     */
    @GetMapping("/{id}/edit")
    public String editScreenForm(@PathVariable Long id, Model model) {
        Screen screen = screenService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid screen ID: " + id));
        
        model.addAttribute("screen", screen);
        model.addAttribute("theaters", theaterService.findAll());
        model.addAttribute("screenTypes", screenTypeService.findAll());
        model.addAttribute("seatTypes", seatTypeService.findAll());
        
        return "admin/screen-form";
    }

    /**
     * Create/Update Screen
     */
    @PostMapping("/save")
    public String saveScreen(@ModelAttribute Screen screen, RedirectAttributes redirectAttributes) {
        try {
            screenService.save(screen);
            redirectAttributes.addFlashAttribute("success", 
                screen.getId() == null ? "Thêm phòng chiếu thành công!" : "Cập nhật phòng chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/screens";
    }

    /**
     * Delete Screen
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteScreen(@PathVariable Long id) {
        try {
            screenService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể xóa phòng chiếu này. Có thể nó đang được sử dụng.");
        }
    }
}
