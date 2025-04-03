package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Screening;
import com.viecinema.moviebooking.model.Showtime;
import com.viecinema.moviebooking.repository.ScreeningRepository;
import com.viecinema.moviebooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private ScreeningRepository screeningRepository;

    /**
     * Tìm kiếm lịch chiếu với bộ lọc
     */
    public Page<Showtime> findShowtimes(Long movieId, Long theaterId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // Sử dụng phương thức repository để tìm kiếm với các bộ lọc
        return showtimeRepository.findShowtimes(movieId, theaterId, startDate, endDate, pageable);
    }

    /**
     * Tìm lịch chiếu theo ID
     */
    public Optional<Showtime> findById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Page<Showtime> findAllByFilters(Long movieId, Long theaterId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return showtimeRepository.findShowtimes(movieId, theaterId, startDate, endDate, pageable);
    }
    /**
     * Lưu hoặc cập nhật lịch chiếu
     */
    public Showtime save(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    /**
     * Xóa lịch chiếu
     */
    public void deleteById(Long id) {
        showtimeRepository.deleteById(id);
    }

    /**
     * TODO: Implement this method
     * Tính tỷ lệ đặt vé trên toàn hệ thống
     * Giả lập tỷ lệ đặt vé dựa trên dữ liệu sẵn có
     */
    public double calculateBookingRate() {
        // Trong trường hợp thực tế, cần thực hiện các truy vấn phức tạp hơn
        // để tính toán tỷ lệ đặt vé cho mỗi suất chiếu
        // Ở đây chúng ta trả về một giá trị mẫu
        return 75.0; // Giả sử tỷ lệ đặt vé là 75%
    }
    
    /**
     * Lấy danh sách lịch chiếu của một phim
     */
    public List<Showtime> findByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }
    
    /**
     * Lấy danh sách lịch chiếu của một phim (Integer version)
     */
    public List<Showtime> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }
    
    /**
     * Lấy danh sách lịch chiếu của một rạp
     * Hiện tại chưa có truy vấn trực tiếp cho rạp, thực hiện lọc thủ công
     */
    public List<Showtime> findByTheaterId(Long theaterId) {
        List<Showtime> allShowtimes = showtimeRepository.findAll();
        
        return allShowtimes.stream()
            .filter(showtime -> showtime.getScreenings().stream()
                .anyMatch(screening -> screening.getScreen().getTheater().getTheaterId().equals(theaterId.intValue())))
            .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra xem có lịch chiếu nào bị xung đột với khung giờ đã chọn
     */
    public boolean hasConflictingScreening(Screen screen, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Sửa lại cách lấy screenings cho một màn hình và ngày cụ thể
        List<Screening> screenings = screeningRepository.findByScreen(screen).stream()
            .filter(screening -> screening.getScreeningDate().equals(date))
            .collect(Collectors.toList());
        
        // Kiểm tra xung đột thời gian: nếu thời gian bắt đầu hoặc kết thúc của suất chiếu mới
        // nằm trong khoảng thời gian của các suất chiếu đã tồn tại
        return screenings.stream().anyMatch(screening -> 
            (startTime.isBefore(screening.getEndTime()) && endTime.isAfter(screening.getStartTime())));
    }
}
