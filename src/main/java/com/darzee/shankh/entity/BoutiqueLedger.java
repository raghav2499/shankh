package com.darzee.shankh.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "boutique_ledger")
@Entity
@Data
@SequenceGenerator(name = "boutique-ledger-seq", sequenceName = "boutique_ledger_seq", allocationSize = 1)
public class BoutiqueLedger extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boutique-ledger-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "boutique_id")
    private Long boutiqueId;

    @Column(name = "monthly_amount_recieved")
    private Double monthlyAmountRecieved;

    @Column(name = "monthly_pending_amount")
    private Double monthlyPendingAmount;

    @Column(name = "total_amount_recieved")
    private Double totalAmountRecieved;

    @Column(name = "total_pending_amount")
    private Double totalPendingAmount;

    @Column(name = "monthly_active_orders")
    private Integer monthlyActiveOrders;

    @Column(name = "monthly_closed_orders")
    private Integer monthlyClosedOrders;

    @Column(name = "total_active_orders")
    private Integer totalActiveOrders;

    @Column(name = "total_closed_orders")
    private Integer totalClosedOrders;
}
