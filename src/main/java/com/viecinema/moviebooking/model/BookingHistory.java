package com.viecinema.moviebooking.model;

import jakarta.persistence.*;

@Table(name = "booking_history", indexes = {
        @Index(name = "booking_id_index", columnList = "booking_id"),
        @Index(name = "user_id_index", columnList = "user_id")
})
public class BookingHistory {
}
