package com.darzee.shankh.controller;

import com.darzee.shankh.request.OrderCreationRequest;
import com.darzee.shankh.request.RecievePaymentRequest;
import com.darzee.shankh.response.GetOrderResponse;
import com.darzee.shankh.response.OrderSummary;
import com.darzee.shankh.service.OrderOrderItemCommonService;
import com.darzee.shankh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderOrderItemCommonService orderOrderItemCommonService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<OrderSummary> createOrder(@Valid @RequestBody OrderCreationRequest request) throws Exception {
        OrderSummary response = orderOrderItemCommonService.createOrder(request);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<GetOrderResponse> getOrder(@RequestParam(name = "boutique_id") Long boutiqueId,
                                                     @RequestParam(name = "order_item_status_list", required = false) String orderItemStatusList,
                                                     @RequestParam(name = "customer_id", required = false) Long customerId,
                                                     @RequestParam(name = "delivery_date_from", required = false) String deliveryDateFrom,
                                                     @RequestParam(name = "delivery_date_till", required = false) String deliveryDateTill,
                                                     @RequestParam(name = "priority_orders_only", required = false) Boolean priorityOrdersOnly,
                                                     @RequestParam(name = "payment_due", required = false) Boolean paymentDue,
                                                     @RequestParam(name = "order_status_list", required = false) String orderStatusList,
//                                                     @RequestParam(name = "sort_key", required = false, defaultValue = "trial_date") String sortKey,
                                                     @RequestParam(name = "count", required = false, defaultValue = "10") Integer countPerPage,
                                                     @RequestParam(name = "page_count", required = false, defaultValue = "1") Integer pageCount) {
        return orderService.getOrder(boutiqueId, orderItemStatusList, orderStatusList,
                priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill, paymentDue, countPerPage, pageCount);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderDetails(@PathVariable("id") Long orderId) throws Exception {
        return orderService.getOrderDetails(orderId);
    }

//    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin
//    public ResponseEntity updateOrder(@PathVariable("id") Long orderId,
//                                      @Valid @RequestBody UpdateOrderRequest request) {
//        return orderService.updateOrder(orderId, request);
//    }

    @PostMapping(value = "/{id}/recieve_payment", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity recievePayment(@PathVariable("id") Long orderId,
                                         @Validated @RequestBody RecievePaymentRequest request) {
        return orderService.recieveOrderPayment(orderId, request);
    }

    @GetMapping(value = "/{id}/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getInvoice(@PathVariable("id") Long orderId) {
        return orderService.getOrderInvoiceLink(orderId);
    }

    @PostMapping(value = "/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity confirmOrder(@PathVariable("id") Long orderId) {
        return orderService.confirmOrder(orderId);
    }
}
