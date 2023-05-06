package com.darzee.shankh.controller;

import com.darzee.shankh.request.CreateOrderRequest;
import com.darzee.shankh.request.GetOrderDetailsRequest;
import com.darzee.shankh.request.UpdateOrderRequest;
import com.darzee.shankh.response.CreateOrderResponse;
import com.darzee.shankh.response.GetOrderResponse;
import com.darzee.shankh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) throws Exception {
        ResponseEntity<CreateOrderResponse> response = orderService.createNewOrder(request);
        return response;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrderResponse> getOrder(@RequestParam(name = "boutique_id") Long boutiqueId,
                                                     @RequestParam(name = "status_list") String statusList,
                                                     @RequestParam(name = "customer_id", required = false) Long customerId,
                                                     @RequestParam(name = "delivery_date_from", required = false) String deliveryDateFrom,
                                                     @RequestParam(name = "delivery_date_till", required = false) String deliveryDateTill,
                                                     @RequestParam(name = "priority_orders_only", required = false) Boolean priorityOrdersOnly,
                                                     @RequestParam(name = "sort_key", required = false, defaultValue = "trial_date") String sortKey,
                                                     @RequestParam(name = "count", required = false, defaultValue = "10") Integer countPerPage,
                                                     @RequestParam(name = "page_count", required = false, defaultValue = "1") Integer pageCount) {
        Map<String, Object> filterMap = GetOrderDetailsRequest.getFilterMap(boutiqueId, statusList, priorityOrdersOnly,
                customerId, deliveryDateFrom, deliveryDateTill);
        Integer offset = (pageCount - 1) * countPerPage;
        Map<String, Object> pagingCriteriaMap = GetOrderDetailsRequest.getPagingAndSortCriteria(sortKey, countPerPage, offset);
        return orderService.getOrder(filterMap, pagingCriteriaMap);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOrderDetails(@PathVariable("id") Long orderId) throws Exception {
        return orderService.getOrderDetails(orderId);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateOrder(@PathVariable("id") Long orderId,
                                      @RequestBody UpdateOrderRequest request) {
        return orderService.updateOrder(orderId, request);
    }
}
