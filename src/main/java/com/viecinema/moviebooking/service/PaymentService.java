package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Payment;
import com.viecinema.moviebooking.model.PaymentMethod;
import com.viecinema.moviebooking.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    Payment savePayment(Payment payment);
    Payment getPaymentById(Long paymentId);
    List<Payment> getAllPayments();

    List<Payment> getPaymentsByBookingId(Long bookingId);

    List<Payment> getPaymentsByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> getPaymentsByPaymentStatus(PaymentStatus paymentStatus);
    void deletePayment(Long paymentId);
}
