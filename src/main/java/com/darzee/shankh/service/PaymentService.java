package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.PaymentDAO;
import com.darzee.shankh.entity.Payment;
import com.darzee.shankh.enums.PaymentMode;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PaymentRepo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public PaymentDAO recordPayment(Double amount, PaymentMode paymentMode, Boolean isAdvancePayment,
            OrderDAO order) {
        PaymentDAO paymentDAO = new PaymentDAO(amount, paymentMode, isAdvancePayment, order);
        paymentDAO = mapper.paymentToPaymentDAO(paymentRepo.save(mapper.paymentDAOToPayment(paymentDAO,
                new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
        return paymentDAO;
    }

    // public PaymentDAO updateAdvancePayment(OrderDAO order, Double finalAmount) {
    // List<Payment> orderPayments = paymentRepo.findAllByOrderId(order.getId());
    // CycleAvoidingMappingContext cycleAvoidingMappingContext = new
    // CycleAvoidingMappingContext();
    // PaymentDAO finalPayment = null;
    // if (!Collections.isEmpty(orderPayments)) {
    // List<PaymentDAO> advancePayments = orderPayments.stream()
    // .map(payment -> mapper.paymentToPaymentDAO(payment,
    // cycleAvoidingMappingContext))
    // .filter(paymentDAO -> Boolean.TRUE.equals(paymentDAO.getIsAdvancePayment()))
    // .collect(Collectors.toList());
    // if (!Collections.isEmpty(advancePayments)) {
    // advancePayments.sort(Comparator.comparing((payment -> payment.getId())));
    // finalPayment =
    // reversePaymentAndUpdateAmount(advancePayments.get(advancePayments.size() -
    // 1),
    // finalAmount, Boolean.TRUE);
    // return finalPayment;
    // }
    // }
    // finalPayment = new PaymentDAO(finalAmount, PaymentMode.CASH, Boolean.TRUE,
    // order);
    // paymentRepo.save(mapper.paymentDAOToPayment(finalPayment, new
    // CycleAvoidingMappingContext()));
    // return finalPayment;
    // }

    public PaymentDAO reversePaymentAndUpdateAmount(PaymentDAO paymentDAO, Double finalPaymentAmount,
            Boolean isAdvancePayment) {
        PaymentDAO reversePayment = PaymentDAO.reversePayment(paymentDAO);
        paymentRepo.save(mapper.paymentDAOToPayment(reversePayment, new CycleAvoidingMappingContext()));
        if (finalPaymentAmount > 0) {
            PaymentDAO finalPayment = new PaymentDAO(finalPaymentAmount, paymentDAO.getPaymentMode(), isAdvancePayment,
                    paymentDAO.getOrder());
            paymentRepo.save(mapper.paymentDAOToPayment(finalPayment, new CycleAvoidingMappingContext()));
            return finalPayment;
        }
        return null;
    }

    public PaymentDAO getPaymentDAOByOrderId(Long orderId) {
        Optional<Payment> payment = paymentRepo.findTopByOrderIdOrderByIdDesc(orderId);
        if (payment.isPresent()) {
            return mapper.paymentToPaymentDAO(payment.orElse(null), new CycleAvoidingMappingContext());
        }
        return null;
    }
}
