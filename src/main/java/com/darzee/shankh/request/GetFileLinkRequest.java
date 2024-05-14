package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class GetFileLinkRequest {
    @NotNull(message = "entity_type is mandatory to get file link")
    private String entityType;
    @NotNull(message = "entity_id is mandatory to get file link")
    private Long entityId;
    private GetFileLinkMeta metaData;
}
