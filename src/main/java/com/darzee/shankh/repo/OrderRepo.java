package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByBoutiqueIdAndOrderStatus(Long boutiqueId, OrderStatus status);

    List<Order> findAllByBoutiqueIdAndOrderStatusIn(Long boutiqueId, List<OrderStatus> status);

}
