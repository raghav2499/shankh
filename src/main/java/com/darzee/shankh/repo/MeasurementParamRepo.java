package com.darzee.shankh.repo;

import com.darzee.shankh.entity.MeasurementParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementParamRepo extends JpaRepository<MeasurementParam, Long> {
    List<MeasurementParam> findAllByNameIn(List<String> name);
}
