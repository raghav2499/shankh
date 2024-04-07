package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class ItemsCount {
    private Integer newItemsCount;
    private Integer closedItemsCount;

    public ItemsCount(Pair<Integer, Integer> newAndClosedItemPair) {
        this.newItemsCount = newAndClosedItemPair.getFirst();
        this.closedItemsCount = newAndClosedItemPair.getSecond();
    }
}
