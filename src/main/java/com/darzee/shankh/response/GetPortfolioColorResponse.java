package com.darzee.shankh.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPortfolioColorResponse {
    private Map<Integer, String> colorMap;
}
