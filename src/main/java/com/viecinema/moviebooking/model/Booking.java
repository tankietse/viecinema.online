package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "user_id_index", columnList = "user_id"),
        @Index(name = "booking_date_index", columnList = "booking_date"),
        @Index(name = "showtime_id_index", columnList = "showtime_id")
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;
    
    // Helper method to get total amount as double for calculations
    public double getTotalAmount() {
        return totalPrice != null ? totalPrice.doubleValue() : 0.0;
    }
    
    // Helper method to get seats (placeholder - implement based on your data model)
    public String getSeats() {
        // This should return a formatted string of seats or be replaced with proper implementation
        return ""; // Placeholder implementation
    }
    
    // Helper fields for compatibility with existing service
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
