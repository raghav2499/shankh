package com.darzee.shankh.service;

import com.darzee.shankh.dao.OrderDAO;
import com.darzee.shankh.dao.PaymentDAO;
import com.darzee.shankh.entity.Payment;
import com.darzee.shankh.enums.PaymentMode;
import com.darzee.shankh.mapper.CycleAvoidingMappingContext;
import com.darzee.shankh.mapper.DaoEntityMapper;
import com.darzee.shankh.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private DaoEntityMapper mapper;

    public PaymentDAO recordPayment(Double amount, PaymentMode paymentMode, Boolean isAdvancePayment, OrderDAO order) {
        PaymentDAO paymentDAO = new PaymentDAO(amount, paymentMode, isAdvancePayment, order);
        paymentDAO = mapper.paymentToPaymentDAO(paymentRepo.save(mapper.paymentDAOToPayment(paymentDAO, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
        return paymentDAO;
    }

    public PaymentDAO reversePaymentAndUpdateAmount(PaymentDAO paymentDAO, Double finalPaymentAmount, Boolean isAdvancePayment) {
        PaymentDAO reversePayment = PaymentDAO.reversePayment(paymentDAO);
        paymentRepo.save(mapper.paymentDAOToPayment(reversePayment, new CycleAvoidingMappingContext()));
        if (finalPaymentAmount > 0) {
            PaymentDAO finalPayment = new PaymentDAO(finalPaymentAmount, paymentDAO.getPaymentMode(), isAdvancePayment, paymentDAO.getOrder());
            paymentRepo.save(mapper.paymentDAOToPayment(finalPayment, new CycleAvoidingMappingContext()));
            return finalPayment;
        }
        return null;
    }

    public PaymentDAO getPaymentDAOByOrderId(Long orderId) {
        Optional<Payment> payment = paymentRepo.findTopByOrderIdOrderByIdDesc(orderId);
        if (payment.isPresent()) {
            return mapper.paymentToPaymentDAO(payment.get(), new CycleAvoidingMappingContext());
        }
        return null;
    }
}
