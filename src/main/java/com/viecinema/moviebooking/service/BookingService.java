package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.BookingStatus;
import com.viecinema.moviebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    /**
     * Lấy tất cả các đặt vé
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * Lấy đặt vé theo ID
     */
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElse(null);
    }
    
    /**
     * Tạo đặt vé mới
     */
    public Booking createBooking(Booking booking) {
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }
    
    /**
     * Cập nhật trạng thái đặt vé
     */
    public Booking updateBookingStatus(Integer id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setStatus(status);
            booking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }
    
    /**
     * Xóa đặt vé
     */
    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }
    
    /**
     * Lấy danh sách đặt vé của người dùng
     */
    public List<Booking> getUserBookings(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    /**
     * Lấy danh sách đặt vé của suất chiếu
     */
    public List<Booking> getShowtimeBookings(Integer showtimeId) {
        return bookingRepository.findByShowtimeShowtimeId(showtimeId);
    }
    
    /**
     * Lấy doanh thu tháng hiện tại
     */
    public double getCurrentMonthRevenue() {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay();
        
        List<Booking> bookings = bookingRepository.findByStatusAndCreatedAtBetween(
                BookingStatus.COMPLETED, startOfMonth, endOfMonth);
        
        return bookings.stream()
                .mapToDouble(Booking::getTotalAmount)
                .sum();
    }
    
    /**
     * Lấy doanh thu năm hiện tại
     */
    public double getYearlyRevenue() {
        int currentYear = LocalDate.now().getYear();
        LocalDateTime startOfYear = LocalDate.of(currentYear, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(currentYear, 12, 31).plusDays(1).atStartOfDay();
        
        List<Booking> bookings = bookingRepository.findByStatusAndCreatedAtBetween(
                BookingStatus.COMPLETED, startOfYear, endOfYear);
        
        return bookings.stream()
                .mapToDouble(Booking::getTotalAmount)
                .sum();
    }
    
    /**
     * Đếm số lượng đặt vé đang chờ xử lý
     */
    public int countPendingBookings() {
        return bookingRepository.countByStatus(BookingStatus.PENDING);
    }
    
    /**
     * Lấy doanh thu theo tháng trong năm hiện tại
     */
    public Map<String, Double> getMonthlyRevenue() {
        int currentYear = LocalDate.now().getYear();
        Map<String, Double> monthlyRevenue = new HashMap<>();
        
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();
            
            List<Booking> bookings = bookingRepository.findByStatusAndCreatedAtBetween(
                    BookingStatus.COMPLETED, startOfMonth, endOfMonth);
            
            double revenue = bookings.stream()
                    .mapToDouble(Booking::getTotalAmount)
                    .sum();
            
            monthlyRevenue.put(String.format("%d-%02d", currentYear, month), revenue);
        }
        
        return monthlyRevenue;
    }
    
    /**
     * Lấy lịch sử giao dịch của người dùng
     */
    public List<Map<String, Object>> getUserBookingHistory(Long userId) {
        List<Booking> userBookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId.intValue());
        List<Map<String, Object>> history = new ArrayList<>();
        
        for (Booking booking : userBookings) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", booking.getBookingId());
            entry.put("movieTitle", booking.getShowtime().getMovie().getTitle());
            if (booking.getShowtime().getScreenings() != null && !booking.getShowtime().getScreenings().isEmpty()) {
                entry.put("theater", booking.getShowtime().getScreenings().get(0).getScreen().getTheater().getTheaterName());
            } else {
                entry.put("theater", "Unknown Theater");
            }
            entry.put("showtime", booking.getShowtime().getStartDate());
            entry.put("amount", booking.getTotalAmount());
            entry.put("status", booking.getStatus());
            entry.put("createdAt", booking.getCreatedAt());
            entry.put("seats", booking.getSeats());
            
            history.add(entry);
        }
        
        return history;
    }
    
    /**
     * Lấy hoạt động gần đây trong hệ thống
     */
    public List<Map<String, Object>> getRecentActivities(int limit) {
        List<Booking> recentBookings = bookingRepository.findTop10ByOrderByCreatedAtDesc()
                .stream().limit(limit).collect(Collectors.toList());
        
        List<Map<String, Object>> activities = new ArrayList<>();
        
        for (Booking booking : recentBookings) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "booking");
            activity.put("id", booking.getBookingId());
            activity.put("user", booking.getUser().getUsername());
            activity.put("movie", booking.getShowtime().getMovie().getTitle());
            activity.put("amount", booking.getTotalAmount());
            activity.put("status", booking.getStatus());
            activity.put("time", booking.getCreatedAt());
            
            activities.add(activity);
        }
        
        return activities;
    }
}
