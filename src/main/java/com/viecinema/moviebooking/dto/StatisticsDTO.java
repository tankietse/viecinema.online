package com.viecinema.moviebooking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatisticsDTO {
    private Long id;
    private java.sql.Date date;
    private Long movieId;
    private BigDecimal totalRevenue;
    private Integer ticketsSold;
}