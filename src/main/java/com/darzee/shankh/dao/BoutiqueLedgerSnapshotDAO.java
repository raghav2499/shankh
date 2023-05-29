package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoutiqueLedgerSnapshotDAO {

    private Long id;

    private Double pendingAmount;

    private Double amountRecieved;

    private Integer activeOrderCount;

    private Long boutiqueId;

    private Integer month;

    private Integer year;

    public BoutiqueLedgerSnapshotDAO(Double pendingAmount,
                                     Double amountRecieved,
                                     Integer activeOrderCount,
                                     Long boutiqueId,
                                     Integer month,
                                     Integer year) {
        this.pendingAmount = pendingAmount;
        this.amountRecieved = amountRecieved;
        this.activeOrderCount = activeOrderCount;
        this.boutiqueId = boutiqueId;
        this.month = month;
        this.year = year;
    }
}
