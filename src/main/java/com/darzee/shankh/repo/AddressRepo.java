package com.darzee.shankh.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darzee.shankh.entity.Address;



public interface AddressRepo extends JpaRepository<Address, Long> {

} 
