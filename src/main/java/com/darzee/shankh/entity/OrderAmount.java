package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "order_amount")
@Entity
@SequenceGenerator(name = "order-amount-seq", sequenceName = "order_amount_seq", allocationSize = 1)
public class OrderAmount extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-amount-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "amount_recieved")
    private Double amountRecieved;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
