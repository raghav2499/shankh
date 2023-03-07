package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findAllByBoutiqueId(Long boutiqueId);
}
