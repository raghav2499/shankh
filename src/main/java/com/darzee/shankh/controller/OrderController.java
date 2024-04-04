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


@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderOrderItemCommonService orderOrderItemCommonService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<OrderSummary> createOrder(@Validated(OrderCreationRequest.CreateOrder.class) @RequestBody OrderCreationRequest request) throws Exception {
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
                                                     @RequestParam(name = "order_status_list", required = false, defaultValue = "1,2,3") String orderStatusList,
                                                     @RequestParam(name = "count", required = false, defaultValue = "10") Integer countPerPage,
                                                     @RequestParam(name = "page_count", required = false, defaultValue = "1") Integer pageCount) {
        return orderService.getOrder(boutiqueId, orderItemStatusList, orderStatusList,
                priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill, paymentDue, countPerPage, pageCount);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderDetails(@PathVariable("id") Long boutiqueOrderId, 
                                          @RequestParam("boutique_id") Long boutiqueId) throws Exception {
        return orderService.getOrderDetails(boutiqueOrderId, boutiqueId);
    }

    @PostMapping(value = "/{id}/recieve_payment", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity recievePayment(@PathVariable("id") Long boutiqueOrderId,
                                         @RequestParam("boutique_id") Long boutiqueId,
                                         @Validated @RequestBody RecievePaymentRequest request) {
        return orderService.recieveOrderPayment(boutiqueOrderId, boutiqueId, request);
    }

    @PostMapping(value = "/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity confirmOrder(@PathVariable("id") Long boutiqueOrderId,
                                       @RequestParam("boutique_id") Long boutiqueId,
                                       @Validated(OrderCreationRequest.ConfirmOrder.class) @RequestBody OrderCreationRequest request) {
        return orderService.confirmOrderAndGenerateInvoice(boutiqueOrderId, boutiqueId, request);
    }

    @GetMapping(value = "/{id}/invoice_detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getInvoiceDetail(@PathVariable("id") Long boutiqueOrderId,
                                           @RequestParam("boutique_id") Long boutiqueId) {
        return orderService.getInvoiceDetail(boutiqueOrderId, boutiqueId);
    }
}
