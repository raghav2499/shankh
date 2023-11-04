package com.darzee.shankh.entity;

import com.darzee.shankh.enums.ColorEnum;
import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Enumerated(EnumType.ORDINAL)
    private OutfitType outfitType;

    @Column(name = "sub_outfit_type")
    private Integer subOutfitType;

    @Column(name = "color")
    @Enumerated(EnumType.ORDINAL)
    private ColorEnum color;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_valid")
    private Boolean isValid;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

}
