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
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long id) {
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
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id, @RequestBody Theater theaterDetails) {
        return theaterService.getTheaterById(id)
                .map(theater -> {
                    theater.setName(theaterDetails.getName());
                    theater.setAddress(theaterDetails.getAddress());
                    theater.setCity(theaterDetails.getCity());
                    theater.setState(theaterDetails.getState());
                    theater.setPostalCode(theaterDetails.getPostalCode());
                    theater.setCountry(theaterDetails.getCountry());
                    theater.setPhoneNumber(theaterDetails.getPhoneNumber());
                    theater.setCapacity(theaterDetails.getCapacity());
                    return ResponseEntity.ok(theaterService.saveTheater(theater));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
