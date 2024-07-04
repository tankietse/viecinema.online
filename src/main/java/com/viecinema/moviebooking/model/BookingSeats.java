package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_seats", indexes = {
        @Index(name = "booking_id_index", columnList = "booking_id"),
        @Index(name = "seat_id_index", columnList = "seat_id")
})
public class BookingSeats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Integer bookingSeatId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "price")
    private BigDecimal price; // Sử dụng BigDecimal cho giá tiền
}

