package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreateOrderItemRequest;
<<<<<<< HEAD
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.innerObjects.UpdateOrderItemDetails;
import com.darzee.shankh.response.*;
=======
import com.darzee.shankh.request.innerObjects.UpdateOrderItemDetailRequest;
import com.darzee.shankh.response.CreateOrderResponse;
import com.darzee.shankh.response.OrderItemSummary;
import com.darzee.shankh.response.OrderSummary;
>>>>>>> acc2ce8b52ff86ee10ada897ef550f2a920c0547
import com.darzee.shankh.service.OrderItemService;
import com.darzee.shankh.service.OrderOrderItemCommonService;
import com.darzee.shankh.service.StitchOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order_item")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderOrderItemCommonService orderOrderItemCommonService;

    @Autowired
    private StitchOptionService stitchOptionService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<CreateOrderResponse> createOrderItem(@Valid @RequestBody CreateOrderItemRequest request) {
        OrderSummary orderSummary = orderOrderItemCommonService.createOrderItem(request);
        CreateOrderResponse createOrderResponse = new CreateOrderResponse("Order created successfully",
                orderSummary);
        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<OrderItemSummary> updateOrderItem(@PathVariable("id") Long orderItemId,
                                                            @Valid @RequestBody UpdateOrderItemDetailRequest orderItemDetails) {
        OrderItemSummary orderItemSummary = orderOrderItemCommonService.updateOrderItem(orderItemId, orderItemDetails);
        return new ResponseEntity<>(orderItemSummary, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderItem(@PathVariable("id") Long orderItemId) {
        return new ResponseEntity<>(orderItemService.getOrderItemDetails(orderItemId), HttpStatus.OK);
    }

    @PostMapping(value = "/stitch_options", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<CreateStitchResponse> createStitchOptions(@Valid @RequestBody CreateStitchOptionRequest request) {
        StitchSummary stitchSummary  = stitchOptionService.createStitchOptions(request);
        CreateStitchResponse createStitchResponse = new CreateStitchResponse("Stitch options created successfully",
                stitchSummary);
        return new ResponseEntity<>(createStitchResponse, HttpStatus.CREATED);
    }
}
