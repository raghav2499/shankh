package com.darzee.shankh.dao;

import com.darzee.shankh.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDAO {
    private Long id;
    private PaymentMode paymentMode = PaymentMode.CASH;
    private Double amount;
    private LocalDate paymentDate = LocalDate.now();
    private Boolean isAdvancePayment = Boolean.FALSE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderDAO order;

    public static PaymentDAO reversePayment(PaymentDAO payment) {
        PaymentDAO paymentCopy = new PaymentDAO();
        paymentCopy.setPaymentMode(payment.getPaymentMode());
        paymentCopy.setAmount(-payment.getAmount());
        paymentCopy.setIsAdvancePayment(payment.getIsAdvancePayment());
        paymentCopy.setOrder(payment.getOrder());
        return paymentCopy;
    }

    public PaymentDAO(Double amount, PaymentMode paymentMode, Boolean isAdvancePayment,
                      OrderDAO order) {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setPaymentDate(paymentDate);
        paymentDAO.setPaymentMode(paymentMode);
        paymentDAO.setAmount(amount);
        paymentDAO.setIsAdvancePayment(isAdvancePayment);
        paymentDAO.setOrder(order);
    }
}
