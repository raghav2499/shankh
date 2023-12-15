package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Table(name = "measurements")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "measurements-seq", sequenceName = "measurements_seq", allocationSize = 1)
public class Measurements extends GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurements-seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "outfit_type")
    @Enumerated(EnumType.ORDINAL)
    private OutfitType outfitType;

    @Type(type = "jsonb")
    @Column(name = "measurement_value", columnDefinition = "jsonb")
    private Map<String, Double> measurementValue;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "measurement_revision_id")
    private MeasurementRevisions measurementRevision;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}