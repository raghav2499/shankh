package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "tailor")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "tailor-seq", sequenceName = "tailor_seq", allocationSize = 1)
public class Tailor extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tailor-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pay")
    private Integer pay;

    @Column(name = "is_owner")
    private Boolean isOwner = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;


}
