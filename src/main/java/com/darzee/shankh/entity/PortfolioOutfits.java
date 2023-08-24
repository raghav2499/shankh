package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "portfolio_outfits")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "portfolio-outfits-seq", sequenceName = "portfolio_outfits_seq", allocationSize = 1)
public class PortfolioOutfits extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-outfits-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "outfit_type")
    private OutfitType outfitType;

    @Column(name = "sub_outfit_type")
    private Integer subOutfitType;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

}
