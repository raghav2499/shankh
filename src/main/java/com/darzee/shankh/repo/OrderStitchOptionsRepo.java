package com.darzee.shankh.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darzee.shankh.entity.OrderStitchOptions;

@Repository
public interface OrderStitchOptionsRepo extends JpaRepository<OrderStitchOptions, Long> {

}
