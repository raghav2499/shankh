package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "price_breakup")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "price-breakup-seq", sequenceName = "price_breakup_seq", allocationSize = 1)
public class PriceBreakup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price-breakup-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "component", nullable = false)
    private String component;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}
