package com.darzee.shankh.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPortfolioColorResponse {
    private List<PortfolioColorDetail> colorDetail;
}
