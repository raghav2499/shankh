package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByBoutiqueIdAndOrderStatus(Long boutiqueId, OrderStatus status);

    List<Order> findAllByBoutiqueIdAndOrderStatusIn(Long boutiqueId, List<OrderStatus> status);

}
