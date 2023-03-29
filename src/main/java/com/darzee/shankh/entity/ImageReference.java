package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "image_reference")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "img-ref-seq", sequenceName = "img_ref_seq", allocationSize = 1)
public class ImageReference extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "img-ref-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Column(name = "image_name")
    private String imageName;
}
