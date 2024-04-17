package com.darzee.shankh.controller;

import com.darzee.shankh.repo.OrderItemRepo;
import com.darzee.shankh.request.CreateStitchOptionRequest;
import com.darzee.shankh.request.OrderCreationRequest;
import com.darzee.shankh.request.innerObjects.OrderItemDetailRequest;
import com.darzee.shankh.response.*;
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
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderOrderItemCommonService orderOrderItemCommonService;

    @Autowired
    private StitchOptionService stitchOptionService;
    @Autowired
    private OrderItemRepo orderItemRepo;

    @PostMapping(value = "/order_item/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<CreateOrderResponse> createOrderItem(@Valid @RequestBody OrderCreationRequest request) {
        OrderSummary orderSummary = orderOrderItemCommonService.createOrderItem(request);
        CreateOrderResponse createOrderResponse = new CreateOrderResponse("Order created successfully",
                orderSummary);
        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/order_item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<OrderSummary> updateOrderItem(@PathVariable("id") Long orderItemId,
                                                        @Valid @RequestBody OrderItemDetailRequest orderItemDetails) throws Exception {
        OrderSummary orderSummary = orderOrderItemCommonService.updateOrderItem(orderItemId, orderItemDetails);
        return new ResponseEntity<>(orderSummary, HttpStatus.OK);
    }

    @GetMapping(value = "/order_item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderItem(@PathVariable("id") Long orderItemId) throws Exception {
        return new ResponseEntity<>(orderItemService.getOrderItemDetails(orderItemId), HttpStatus.OK);
    }

    @PostMapping(value = "/order_item/stitch_options", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<CreateStitchResponse> createStitchOptions(@Valid @RequestBody CreateStitchOptionRequest request) {
        StitchSummary stitchSummary = stitchOptionService.createStitchOptions(request);
        CreateStitchResponse createStitchResponse = new CreateStitchResponse("Stitch options created successfully",
                stitchSummary);
        return new ResponseEntity<>(createStitchResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/order_item/{id}/stitch_options", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<CreateStitchResponse> updateStitchOptions(@PathVariable("id") Long orderItemId,
                                                                    @Valid @RequestBody CreateStitchOptionRequest request) {
        StitchSummary stitchSummary = stitchOptionService.updateStitchOptions(orderItemId, request);
        CreateStitchResponse createStitchResponse = new CreateStitchResponse("Stitch options created successfully",
                stitchSummary);
        return new ResponseEntity<>(createStitchResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/order_item", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetOrderItemResponse> getOrderItems(@RequestParam(name = "boutique_id", required = false) Long boutiqueId,
                                                              @RequestParam(name = "order_id", required = false) Long orderId,
                                                              @RequestParam(name = "order_item_status_list", required = false, defaultValue = "1,2,3,4,5,6,7") String orderItemStatusList,
                                                              @RequestParam(name = "priority_orders_only", required = false) Boolean priorityOrdersOnly,
                                                              @RequestParam(name = "delivery_date_from", required = false) String deliveryDateFrom,
                                                              @RequestParam(name = "delivery_date_till", required = false) String deliveryDateTill,
                                                              @RequestParam(name = "sort_key", required = false, defaultValue = "id") String sortKey,
                                                              @RequestParam(name = "sort_order", required = false, defaultValue = "desc") String sortOrder,
                                                              @RequestParam(name = "count", required = false, defaultValue = "10") Integer countPerPage,
                                                              @RequestParam(name = "page_count", required = false, defaultValue = "1") Integer pageCount) {
        return orderItemService.getOrderItemDetails(boutiqueId, orderId, orderItemStatusList,
                priorityOrdersOnly, sortKey, sortOrder, countPerPage, pageCount, deliveryDateFrom, deliveryDateTill);
    }

    @GetMapping(value = "/order_item/{id}/stitch_option", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderStitchOption(@PathVariable("id") Long orderItemId) {
        return stitchOptionService.getOrderItemStitchOptionsResponse(orderItemId);
    }
}
