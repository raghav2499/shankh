package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    List<Payment> findAllByOrderId(Long orderId);
}
