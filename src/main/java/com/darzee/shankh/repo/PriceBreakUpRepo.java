package com.darzee.shankh.repo;

import com.darzee.shankh.entity.PriceBreakup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PriceBreakUpRepo extends JpaRepository<PriceBreakup, Long> {

    @Nullable
    List<PriceBreakup> findByOrderItemId(Long orderItemId);
}
