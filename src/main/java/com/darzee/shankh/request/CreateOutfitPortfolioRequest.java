package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class CreateOutfitPortfolioRequest {

    @NotNull(message = "OutfitType is mandatory")
    private Integer outfitType;
    private String title;
    private Integer subOutfits;
    private Integer color;
    private List<String> referenceId;
}
