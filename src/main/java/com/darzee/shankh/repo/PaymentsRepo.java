package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepo extends JpaRepository<Payments, Long> {

}