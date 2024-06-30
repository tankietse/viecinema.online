package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Payment;
import com.viecinema.moviebooking.model.PaymentMethod;
import com.viecinema.moviebooking.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingId(Long bookingId);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}
