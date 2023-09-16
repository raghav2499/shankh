package com.darzee.shankh.dao;

import com.darzee.shankh.enums.BucketName;
import com.darzee.shankh.enums.SampleImageType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class SampleImageReferenceDAO {
    private Long id;
    private String imageReferenceId;
    private BucketName bucketName;
    private SampleImageType imageType;
}
