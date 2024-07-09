package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.ScreenInfoDTO;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public ScreenInfoDTO getScreenInfoByScreenId(Integer screenId) {
        Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        // Truy xuất thông tin cần thiết từ screen và các đối tượng khác
        String screenName = screen.getName();
        String theaterName = screen.getTheater().getTheaterName();
        String theaterAddress = screen.getTheater().getAddress(); // Đảm bảo theater có thông tin địa chỉ !!!!!!!!!!
        Integer capacity = screen.getCapacity();
        Integer seatRow = screen.getSeat().getRowNumber();
        Integer seatCol = screen.getSeat().getSeatNumber();
        String screenType = screen.getScreenType().getTypeName();
        String seatType = screen.getSeat().getSeatType().getTypeName();
        BigDecimal screenPrice = screen.getScreenType().getScreenPrice();
        BigDecimal seatPrice = screen.getSeat().getSeatType().getSeatPrice();

        return new ScreenInfoDTO(screenName, theaterName,theaterAddress, capacity, seatRow, seatCol, screenType, seatType, screenPrice, seatPrice);
    }

}
