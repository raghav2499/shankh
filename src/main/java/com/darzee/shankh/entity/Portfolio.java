package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


@Table(name = "portfolio")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "portfolio-seq", sequenceName = "portfolio_seq", allocationSize = 1)
public class Portfolio extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "username_updates_counts")
    private Integer usernameUpdatesCounts;

    @Column(name = "about_details")
    private String aboutDetails;

    @Type(type = "jsonb")
    @Column(name = "social_media", columnDefinition = "jsonb")
    private Map<String, String> socialMedia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio")
    private List<PortfolioOutfits> portfolioOutfits;

    @OneToOne
    @JoinColumn(name = "tailor_id")
    private Tailor tailor;
}