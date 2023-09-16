package com.darzee.shankh.entity;

import com.darzee.shankh.enums.BucketName;
import com.darzee.shankh.enums.SampleImageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "sample_image_reference")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "sample-image-ref-seq", sequenceName = "sample_img_ref_seq", allocationSize = 1)
public class SampleImageReference {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sample-image-ref-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_reference_id")
    private String imageReferenceId;

    @Column(name = "bucket_name")
    @Enumerated(EnumType.ORDINAL)
    private BucketName bucketName;

    @Column(name = "image_type")
    @Enumerated(EnumType.ORDINAL)
    private SampleImageType imageType;
}
