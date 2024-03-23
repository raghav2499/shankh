package com.darzee.shankh.entity;

import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.enums.StitchOptionType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Table(name = "stitch_options")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "stitch-options-seq", sequenceName = "stitch_options_seq", allocationSize = 1)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StitchOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stitch-options-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "outfit_type")
    @Enumerated(EnumType.ORDINAL)
    private OutfitType outfitType;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private StitchOptionType type;

    @Column(name = "key")
    private String key;

    @Type(type = "jsonb")
    @Column(name = "value", columnDefinition = "jsonb")
    private List<String> value;

    @Column(name = "outfit_side")
    @Enumerated(EnumType.ORDINAL)
    private OutfitSide outfitSide;

    @Column(name = "is_valid")
    private Boolean isValid;
}
