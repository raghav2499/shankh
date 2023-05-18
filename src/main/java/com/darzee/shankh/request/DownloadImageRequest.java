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
public class DownloadImageRequest {

    @NotNull(message = "file_reference_ids are mandatory for download image request")
    List<String> fileReferenceIds;
}
