package com.viecinema.moviebooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats", indexes = {
        @Index(name = "screen_id_index", columnList = "screen_id"),
        @Index(name = "seat_type_id_index", columnList = "seat_type_id")
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Column(name = "row_name")
    private String rowName;

    @Column(name = "seat_number")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;
}