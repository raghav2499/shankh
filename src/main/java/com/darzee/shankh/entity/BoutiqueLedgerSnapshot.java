package com.darzee.shankh.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "boutique_ledger_snapshot")
@Entity
@Data
@SequenceGenerator(name = "boutique-ledger-snapshot-seq", sequenceName = "boutique_ledger_snapshot_seq", allocationSize = 1)

public class BoutiqueLedgerSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boutique-ledger-snapshot-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "pending_amount", nullable = false)
    private Double pendingAmount;

    @Column(name = "amount_recieved", nullable = false)
    private Double amountRecieved;

    @Column(name = "active_order_count", nullable = false)
    private Integer activeOrderCount;

    @Column(name = "boutique_id", nullable = false)
    private Long boutiqueId;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

}
