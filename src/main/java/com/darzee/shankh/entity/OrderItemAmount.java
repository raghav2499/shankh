package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "orders")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "order-item-amt-seq", sequenceName = "order_item_amt_seq", allocationSize = 1)
public class OrderItemAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;



}
