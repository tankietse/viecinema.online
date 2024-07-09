package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistics", indexes = @Index(name = "unique_date_movie_theater", columnList = "date, movie_id, theater_id", unique = true))
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_tickets_sold")
    private Integer totalTicketsSold;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "average_ticket_price")
    private BigDecimal averageTicketPrice;

    @Column(name = "promotion_count")
    private Integer promotionCount;

    @Column(name = "showtime_count")
    private Integer showtimeCount;
}

