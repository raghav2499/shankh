package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "object_images")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "shop-images-seq", sequenceName = "shop_images_seq", allocationSize = 1)
public class ObjectImages extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop-images-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "referenceId")
    private String referenceId;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;
}
