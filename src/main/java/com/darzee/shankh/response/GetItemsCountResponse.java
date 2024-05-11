package com.darzee.shankh.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetItemsCountResponse {
    private ItemsCount daily;
    private ItemsCount weekly;
    private ItemsCount total;
}
