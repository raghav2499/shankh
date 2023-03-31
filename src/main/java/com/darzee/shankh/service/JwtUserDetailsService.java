package com.darzee.shankh.service;

import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.entity.Tailor;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.TailorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

/**
 * validates if username is a valid tailor phone number
 * 1. This method can be extended to check if the given number is a valid customer number
 * 2. Role based authorisation should be applied here
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private TailorRepo tailorRepo;

    @Autowired
    private DaoEntityMapper mapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Tailor> optionalTailor = tailorRepo.findByPhoneNumber(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (optionalTailor.isPresent()) {
            TailorDAO tailorDAO = mapper.tailorObjectToDao(optionalTailor.get(), new CycleAvoidingMappingContext());
            return new User(tailorDAO.getPhoneNumber(),
                    encoder.encode(""),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("No valid tailor found with phone number : " + username);
        }
    }
}
