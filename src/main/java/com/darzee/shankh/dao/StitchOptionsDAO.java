package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;
import com.darzee.shankh.enums.StitchOptionType;
import lombok.Data;

import java.util.List;

@Data
public class StitchOptionsDAO {
    private Long id;
    private OutfitType outfitType;
    private StitchOptionType type;
    private String key;
    private List<String> value;
    private OutfitSide outfitSide;
    private Boolean isValid;
    private Long priority;
}
