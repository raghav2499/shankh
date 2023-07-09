package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Table(name = "measurements")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "measurements-seq", sequenceName = "measurements_seq", allocationSize = 1)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
}