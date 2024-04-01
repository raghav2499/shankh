package com.darzee.shankh.response;

import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class InvoiceDetailsResponse {
    
    String invoiceNumber ;
    String invoiceDateTime ;
    
    Integer orderNumber ;
    String boutiqueName ;
    String customerName ;
    OutfitType outfitType ;
    double totalAmount;
    double advancePaid;
    double balanceDue;

    String recieveDateTime ;

    double stichingCost ;
    double materialCost ;


    
}
