package com.darzee.shankh.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAmountDAO {
    private Long id;
    private Double totalAmount = 0d;
    private Double amountRecieved = 0d;
}
