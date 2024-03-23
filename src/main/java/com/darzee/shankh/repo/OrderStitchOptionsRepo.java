package com.darzee.shankh.repo;

import com.darzee.shankh.entity.OrderStitchOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStitchOptionsRepo extends JpaRepository<OrderStitchOptions, Long> {
    List<OrderStitchOptions> findAllByIdIn(List<Long> ids);
    List<OrderStitchOptions> findAllByOrderItemIdIn(List<Long> orderItemIds);

    List<OrderStitchOptions> findAllByOrderItemId(Long orderItemId);
}
