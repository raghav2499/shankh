package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "order_item")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "order-item-seq", sequenceName = "order_item_seq", allocationSize = 1)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-item-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "trial_date")
    private LocalDateTime trialDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "order_type")
    @Enumerated(EnumType.ORDINAL)
    private OrderType orderType;

    @Column(name = "outfit_type")
    @Enumerated(EnumType.ORDINAL)
    private OutfitType outfitType;

    @Column(name = "outfit_alias")
    private String outfitAlias;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "inspiration")
    private String inspiration;

    @Column(name = "is_priority_order")
    private Boolean isPriorityOrder = Boolean.FALSE;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "order_item_status")
    @Enumerated(EnumType.ORDINAL)
    private OrderItemStatus orderItemStatus;

    @OneToOne
    @JoinColumn(name = "measurement_revision_id")
    private MeasurementRevisions measurementRevision;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
    private List<PriceBreakup> priceBreakup;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
