package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.response.CreateOrderResponse;
import com.darzee.shankh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) throws Exception {
        ResponseEntity<CreateOrderResponse> response = orderService.createNewOrder(request);
        System.out.println("response is " + response.getBody());
        return response;
    }
}
