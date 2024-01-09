package com.darzee.shankh.controller;

import com.darzee.shankh.repo.CustomerRepo;
import com.darzee.shankh.request.CreateCustomerRequest;
import com.darzee.shankh.request.UpdateCustomerRequest;
import com.darzee.shankh.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getCustomerDetails(@RequestParam("boutique_id") Long boutiqueId) {
        return customerService.getCustomers(boutiqueId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getCustomerDetail(@PathVariable("id") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity updateCustomer(@PathVariable("id") Long customerId,
                                         @RequestBody @Valid UpdateCustomerRequest request) {
        return customerService.updateCustomer(customerId, request);
    }
}
