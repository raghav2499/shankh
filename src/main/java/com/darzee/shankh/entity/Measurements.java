package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private OutfitType OutfitType;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}