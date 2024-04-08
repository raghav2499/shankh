package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderItemDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemSummary {

    private Long id;
    private String outfitType;
    private String trialDate;
    private String deliveryDate;

    private String outfitAlias;

    private Integer quantity;

    private List<FileDetail> clothImages;
    private List<FileDetail> audio;
    private List<PriceBreakupSummary> priceBreakup;

    public OrderItemSummary(OrderItemDAO orderItem) {
        this.id = orderItem.getId();
        this.deliveryDate = orderItem.getDeliveryDate() != null ? orderItem.getDeliveryDate().toString() : null;
        this.outfitType = orderItem.getOutfitType().getDisplayString();
        this.trialDate = orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null;
        this.quantity = orderItem.getQuantity();
        this.outfitAlias = orderItem.getOutfitAlias();
        this.priceBreakup = orderItem.getActivePriceBreakUpList().stream().map(pb -> new PriceBreakupSummary(pb))
                .collect(Collectors.toList());
    }
}