package com.viecinema.moviebooking.dto;

import java.time.LocalDateTime;

public record BookingHistoryDTO(
        Integer historyId,
        Integer bookingId,
        Integer userId,
        String status,
        LocalDateTime changeDate
) { }

