package com.darzee.shankh.repo;

import com.darzee.shankh.entity.OrderAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAmountRepo  extends JpaRepository<OrderAmount, Long> {
    OrderAmount findByOrderId(Long orderId);
}
