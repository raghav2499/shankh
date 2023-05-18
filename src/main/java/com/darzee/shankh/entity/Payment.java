package com.darzee.shankh.entity;

import com.darzee.shankh.enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "payment")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "payment-seq", sequenceName = "payment_seq", allocationSize = 1)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "is_advance_payment")
    private Boolean isAdvancePayment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
