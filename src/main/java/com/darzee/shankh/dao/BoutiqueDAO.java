package com.darzee.shankh.dao;

import com.darzee.shankh.enums.BoutiqueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoutiqueDAO {

    private Long id;
    private String boutiqueReferenceId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoutiqueType boutiqueType;
    private Integer tailorCount;
    private Integer activeOrders = 0;
    private Integer closedOrders = 0;
    private List<TailorDAO> tailors;
    private List<CustomerDAO> customers;
    private List<OrderDAO> orders;

    public BoutiqueDAO(String boutiqueName, String boutiqueType, String boutiqueReferenceId) {
        this.name = boutiqueName;
        this.boutiqueType = BoutiqueType.getEnumMap().get(boutiqueType);
        this.boutiqueReferenceId = boutiqueReferenceId;
    }
}
