package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    public Optional<Screen> getScreenById(Integer id) {
        return screenRepository.findById(id);
    }

    public Screen saveScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    public void deleteScreen(Integer id) {
        screenRepository.deleteById(id);
    }

    public List<Screen> getScreensByTheater(Theater theater) {
        return screenRepository.findByTheater(theater);
    }
}
