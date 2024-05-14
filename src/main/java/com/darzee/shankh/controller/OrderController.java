package com.darzee.shankh.controller;

import com.darzee.shankh.dao.OrderDAO;
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
        OrderDAO orderDAO = orderOrderItemCommonService.createOrder(request);
        OrderDAO refreshedOrderOb = orderOrderItemCommonService.refresh(orderDAO.getId());//hot refreshed the order to load the properties that were generated in run time like boutique_order_id
        OrderSummary orderSummary = new OrderSummary(refreshedOrderOb.getId(), refreshedOrderOb.getBoutiqueOrderId(),
                refreshedOrderOb.getInvoiceNo(),
                orderDAO.getOrderAmount().getTotalAmount(), orderDAO.getOrderAmount().getAmountRecieved(),
                orderDAO.getNonDeletedItems());
        return new ResponseEntity<OrderSummary>(orderSummary, HttpStatus.CREATED);
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
        return orderService.getOrder(boutiqueId, orderItemStatusList, orderStatusList, priorityOrdersOnly, customerId, deliveryDateFrom, deliveryDateTill, paymentDue, countPerPage, pageCount);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getOrderDetails(@PathVariable("id") Long id,
                                          @RequestParam("boutique_id") Long boutiqueId) throws Exception {
        return orderService.getOrderDetails(id, boutiqueId);
    }

    @PostMapping(value = "/{id}/recieve_payment", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity recievePayment(@PathVariable("id") Long orderId,
                                         @RequestParam("boutique_id") Long boutiqueId,
                                         @Validated @RequestBody RecievePaymentRequest request) throws Exception {

        return orderService.recieveOrderPayment(orderId, boutiqueId, request);
    }

    @PostMapping(value = "/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity confirmOrder(@PathVariable("id") Long orderId,
                                       @RequestParam(value = "boutique_id", required = true) Long boutiqueId,
                                       @Validated(OrderCreationRequest.ConfirmOrder.class) @RequestBody OrderCreationRequest request) throws Exception {
        ResponseEntity response =  orderOrderItemCommonService.confirmOrderAndGenerateInvoice(orderId, boutiqueId, request);
        return response;
    }

    @GetMapping(value = "/{id}/invoice_detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getInvoiceDetail(@PathVariable("id") Long orderId,
                                           @RequestParam("boutique_id") Long boutiqueId) {
        return orderService.getInvoiceDetail(orderId, boutiqueId);
    }

    //to get the invoice details of an order by order id and boutique id
    @GetMapping(value = "/{id}/invoice_details", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity getInvoiceDetails(@PathVariable("id") Long orderId,
                                            @RequestParam(name = "boutique_id") Long boutiqueId) {
        return orderService.getOrderInvoiceDetails(orderId, boutiqueId);
    }
}
