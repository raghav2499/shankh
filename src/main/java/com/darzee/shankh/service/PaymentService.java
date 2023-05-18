package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.PaymentDAO;
import com.darzee.shankh.entity.Payment;
import com.darzee.shankh.enums.PaymentMode;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PaymentRepo;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public PaymentDAO updateAdvancePayment(OrderDAO order, Double finalAmount) {
        List<Payment> orderPayments = paymentRepo.findAllByOrderId(order.getId());
        CycleAvoidingMappingContext cycleAvoidingMappingContext = new CycleAvoidingMappingContext();
        PaymentDAO finalPayment = null;
        if (!Collections.isEmpty(orderPayments)) {
            List<PaymentDAO> advancePayments = orderPayments.stream()
                    .map(payment -> mapper.paymentToPaymentDAO(payment, cycleAvoidingMappingContext))
                    .filter(paymentDAO -> Boolean.TRUE.equals(paymentDAO.getIsAdvancePayment()))
                    .collect(Collectors.toList());
            if (!Collections.isEmpty(advancePayments)) {
                advancePayments.sort(Comparator.comparing((payment -> payment.getId())));
                finalPayment = reversePayment(advancePayments.get(advancePayments.size() - 1),
                        finalAmount, Boolean.TRUE);
                return finalPayment;
            }
        }
        finalPayment = new PaymentDAO(finalAmount, PaymentMode.CASH, Boolean.TRUE, order);
        return finalPayment;
    }

    public PaymentDAO reversePayment(PaymentDAO paymentDAO, Double finalPaymentAmount, Boolean isAdvancePayment) {
        PaymentDAO reversePayment = PaymentDAO.reversePayment(paymentDAO);
        PaymentDAO finalPayment = new PaymentDAO(finalPaymentAmount, paymentDAO.getPaymentMode(), isAdvancePayment, paymentDAO.getOrder());
        paymentRepo.save(mapper.paymentDAOToPayment(reversePayment, new CycleAvoidingMappingContext()));
        paymentRepo.save(mapper.paymentDAOToPayment(finalPayment, new CycleAvoidingMappingContext()));
        return finalPayment;
    }
}
