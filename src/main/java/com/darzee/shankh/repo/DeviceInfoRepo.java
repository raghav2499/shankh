package com.darzee.shankh.repo;

import com.darzee.shankh.entity.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceInfoRepo extends JpaRepository<DeviceInfo, Long> {

    Optional<DeviceInfo> findByTailorId(Long tailorId);
}
