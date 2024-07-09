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
        @Index(name = "seat_type_id_index", columnList = "seat_type_id")
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatId;

    @Column(name = "row_num")
    private int rowNumber;

    @Column(name = "seat_num")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;
}