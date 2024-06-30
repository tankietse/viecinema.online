package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screens")
public class ScreenAPIController {

    @Autowired
    private ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<Screen>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screen> getScreenById(@PathVariable Integer id) {
        return screenService.getScreenById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Screen> createScreen(@RequestBody Screen screen) {
        return ResponseEntity.ok(screenService.saveScreen(screen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Screen> updateScreen(@PathVariable Integer id, @RequestBody Screen screenDetails) {
        return screenService.getScreenById(id)
                .map(screen -> {
                    screen.setName(screenDetails.getName());
                    screen.setTheater(screenDetails.getTheater());
                    screen.setCapacity(screenDetails.getCapacity());
                    return ResponseEntity.ok(screenService.saveScreen(screen));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Integer id) {
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Screen>> getScreensByTheater(@PathVariable Integer theaterId) {
        Theater theater = new Theater();
        theater.setTheaterId(Long.valueOf(theaterId));
        return ResponseEntity.ok(screenService.getScreensByTheater(theater));
    }
}
