package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findAllByBoutiqueId(Long boutiqueId);

    Optional<Customer> findByBoutiqueIdAndPhoneNumber(Long boutiqueId, String phoneNumber);

    Integer countByBoutiqueId(Long boutiqueId);

    Integer countByBoutiqueIdAndCreatedAtAfterAndCreatedAtBefore(Long boutiqueId, LocalDateTime dateStart, LocalDateTime dateEnd);
}
