package com.darzee.shankh.dao;

import com.darzee.shankh.enums.BoutiqueType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoutiqueDAO {

    private Long id;
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

    public BoutiqueDAO(String name, String boutiqueType, Integer tailorCount) {
        this.name = name;
        this.boutiqueType = BoutiqueType.getEnumMap().get(boutiqueType);
        this.tailorCount = tailorCount;
    }

}
