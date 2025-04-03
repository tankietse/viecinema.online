package com.viecinema.moviebooking.model;

/**
 * Enum representing possible booking statuses in the system
 */
public enum BookingStatus {
    PENDING,     // Booking has been created but not yet paid
    CONFIRMED,   // Booking has been paid for but not yet used
    COMPLETED,   // Booking has been used (customer attended the screening)
    CANCELLED,   // Booking was cancelled by the customer
    REFUNDED,    // Booking was cancelled and refunded
    EXPIRED      // Booking was not paid for within the time limit
}