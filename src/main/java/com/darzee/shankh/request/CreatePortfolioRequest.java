package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class CreatePortfolioRequest {

    @NotNull(message = "Tailor ID is mandatory to create portfolio")
    private Long tailorId;
    @NotNull(message = "Username is mandatory to create portfolio")
    private String username;
    private String about;
    private Map<Integer, String> socialMedia;
    private String coverImageReference;
}
