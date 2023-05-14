package com.darzee.shankh.entity;

import com.darzee.shankh.dao.TailorDAO;
import com.darzee.shankh.enums.BoutiqueType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "boutique")
@SequenceGenerator(name = "boutique-seq", sequenceName = "boutique_seq", allocationSize = 1)
@Entity
@Getter
@Setter
public class Boutique extends GenericEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boutique-seq")
    private Long id;

    @Column(name = "boutique_reference_id", nullable = false)
    private String boutiqueReferenceId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boutique")
    private List<Tailor> tailors;

    @Column(name = "tailor_count")
    private Integer tailorCount;

    @Column(name = "boutique_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BoutiqueType boutiqueType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_tailor_id")
    private TailorDAO adminTailor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boutique")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boutique")
    private List<Customer> customers = new ArrayList<>();
}
