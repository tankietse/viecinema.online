package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theaters")
public class TheaterAPIController {

    @Autowired
    private TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Integer id) {
        return theaterService.getTheaterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Theater> getTheaterByName(@PathVariable String name) {
        return theaterService.getTheaterByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        return ResponseEntity.ok(theaterService.saveTheater(theater));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Integer id, @RequestBody Theater theaterDetails) {
        return theaterService.getTheaterById(id)
                .map(theater -> {
                    theater.setTheaterName(theaterDetails.getTheaterName());
                    theater.setAddress(theaterDetails.getAddress());
                    theater.setCity(theaterDetails.getCity());
                    theater.setPhoneNumber(theaterDetails.getPhoneNumber());
                    theater.setCapacity(theaterDetails.getCapacity());
                    return ResponseEntity.ok(theaterService.saveTheater(theater));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Integer id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
