package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.ScreenInfoDTO;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.ScreenType;
import com.viecinema.moviebooking.model.SeatType;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    /**
     * Lấy tất cả phòng chiếu
     */
    public List<Screen> findAll() {
        return screenRepository.findAll();
    }

    /**
     * Lấy phòng chiếu phân trang
     */
    public Page<Screen> findAll(Pageable pageable) {
        return screenRepository.findAll(pageable);
    }

    /**
     * Tìm phòng chiếu theo ID
     */
    public Optional<Screen> findById(Long id) {
        return screenRepository.findById(id.intValue());
    }
    
    /**
     * Tìm phòng chiếu theo ID Integer
     */
    public Optional<Screen> getScreenById(Integer id) {
        return screenRepository.findById(id);
    }
    
    /**
     * Lấy tất cả phòng chiếu (cho API)
     */
    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    /**
     * Lấy phòng chiếu theo rạp (cho API)
     */
    public List<Screen> getScreensByTheater(Theater theater) {
        return screenRepository.findByTheater(theater);
    }

    /**
     * Lấy phòng chiếu theo ID rạp
     */
    public List<Screen> findByTheaterId(Theater theater) {
        return screenRepository.findByTheater(theater);
    }

    /**
     * Lưu hoặc cập nhật phòng chiếu
     */
    public Screen save(Screen screen) {
        return screenRepository.save(screen);
    }
    
    /**
     * Lưu hoặc cập nhật phòng chiếu (cho API)
     */
    public Screen saveScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    /**
     * Xóa phòng chiếu
     */
    public void deleteById(Long id) {
        screenRepository.deleteById(id.intValue());
    }
    
    /**
     * Xóa phòng chiếu theo ID Integer
     */
    public void deleteScreen(Integer id) {
        screenRepository.deleteById(id);
    }

    /**
     * Kiểm tra tên phòng chiếu đã tồn tại trong rạp
     */
    public boolean existsByTheaterAndName(Theater theater, String name) {
        return screenRepository.existsByTheaterAndName(theater, name);
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

    public List<Screen> findByTheater(Theater theater) {
        return screenRepository.findByTheater(theater);
    }
    
    /**
     * Find screens by theater ID with pagination
     */
    public Page<Screen> findByTheater(Long theaterId, Pageable pageable) {
        // Create a query that only filters by theater ID
        return screenRepository.findByTheaterAndScreenType(theaterId.intValue(), null, pageable);
    }

    public Page<Screen> findByTheaterAndScreenType(Theater theater, ScreenType screenType, Pageable pageable) {
        return screenRepository.findByTheaterAndScreenType(theater.getTheaterId(), screenType.getScreenTypeId(), pageable);
    }
    
    /**
     * Find screens by theater ID and screen type ID with pagination
     */
    public Page<Screen> findByTheaterAndScreenType(Long theaterId, Integer screenTypeId, Pageable pageable) {
        return screenRepository.findByTheaterAndScreenType(theaterId.intValue(), screenTypeId, pageable);
    }

    public Page<Screen> findByTheaterAndSeatType(Theater theater, SeatType seatType, Pageable pageable) {
        return screenRepository.findByTheaterAndSeatType(theater.getTheaterId(), seatType.getSeatTypeId(), pageable);
    }
    
    /**
     * Find screens by screen type ID with pagination
     */
    public Page<Screen> findByScreenType(Integer screenTypeId, Pageable pageable) {
        // Create a query that only filters by screen type ID
        return screenRepository.findByTheaterAndScreenType(null, screenTypeId, pageable);
    }

}
