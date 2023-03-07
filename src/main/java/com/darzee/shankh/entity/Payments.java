package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "payment")
@Entity
@Getter
@Setter
public class Payments extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_amount")
    private Double orderAmount;

    @Column(name = "amount_paid")
    private Double amountPaid;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
