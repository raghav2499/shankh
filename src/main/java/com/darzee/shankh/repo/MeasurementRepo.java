package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface MeasurementRepo extends JpaRepository<Measurement, Long> {

    @Nullable
    Measurement findByCustomerId(Long customerId);
}