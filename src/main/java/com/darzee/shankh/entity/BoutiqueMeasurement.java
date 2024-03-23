package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "boutique_measurement_param")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "b-measurement-param-seq", sequenceName = "b_measurement_param_seq", allocationSize = 1)
public class BoutiqueMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "b-measurement-param-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "boutique_id")
    private Long boutiqueId;

    @Column(name = "outfit_type")
    private OutfitType outfitType;

    @Type(type = "jsonb")
    @Column(name = "param", columnDefinition = "jsonb")
    private List<String> param;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
