package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OrderStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "order-seq", sequenceName = "order_seq", allocationSize = 1)
public class Order extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "invoice_no", nullable = false)
    private String invoiceNo;

    @Column(name = "trial_date")
    private LocalDateTime trialDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "order_type")
    @Enumerated(EnumType.ORDINAL)
    private OrderType orderType;

    @Column(name = "OutfitType")
    @Enumerated(EnumType.ORDINAL)
    private OutfitType OutfitType;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "inspiration")
    private String inspiration;

    @Column(name = "order_status")
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus = OrderStatus.STITCHING_NOT_STARTED;
    @ManyToOne
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderAmount> orderAmounts;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}