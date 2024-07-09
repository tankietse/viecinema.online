package com.viecinema.moviebooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ScreenInfoDTO {
    private String screenName;
    private String theaterName;
    private String theaterAddress;
    private Integer capacity;
    private Integer seatRow;
    private Integer seatCol;
    private String screenType;
    private String seatType;
    private BigDecimal screenPrice;
    private BigDecimal seatPrice;
}
