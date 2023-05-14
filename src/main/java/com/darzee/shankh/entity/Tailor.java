package com.darzee.shankh.entity;

import com.darzee.shankh.enums.Language;
import com.darzee.shankh.enums.TailorRole;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pay")
    private Integer pay;

    @Column(name = "language")
    private Language language;

    @Column(name = "role")
    private TailorRole role;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "profile_pic_reference_id")
    private String profilePicReferenceId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;


}
