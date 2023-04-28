package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findAllByBoutiqueId(Long boutiqueId);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    List<Customer> findAllByBoutiqueIdAndPhoneNumber(Long boutiqueId, String phoneNumber);
}
