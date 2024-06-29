package com.darzee.shankh.entity;

import com.darzee.shankh.enums.BoutiqueType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.*;

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

    @Column(name = "tailor_count")
    private Integer tailorCount;

    @Column(name = "boutique_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BoutiqueType boutiqueType;

    @OneToOne
    @JoinColumn(name = "admin_tailor_id")
    private Tailor adminTailor;

    @Column(name = "gst_number", length = 15)
    private String gstNumber;

    @Column(name = "gst_rate")
    private BigDecimal gstRate;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "include_delivery_date")
    private Boolean includeDeliveryDate;

    @Column(name="include_gst_in_price")
    private Boolean includeGstInPrice;

    @Column(name = "boutique_phone_number", length = 10)
    private String boutiquephoneNumber;

    @Column(name = "country_code")
    private String countryCode;
}
