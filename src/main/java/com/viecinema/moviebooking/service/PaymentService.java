package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Payment;
import com.viecinema.moviebooking.model.PaymentMethod;
import com.viecinema.moviebooking.model.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    Payment savePayment(Payment payment);
    Payment getPaymentById(Integer paymentId);
    List<Payment> getAllPayments();

    List<Payment> getPaymentsByBookingId(Integer bookingId);

    List<Payment> getPaymentsByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> getPaymentsByPaymentStatus(PaymentStatus paymentStatus);
    void deletePayment(Integer paymentId);
}
