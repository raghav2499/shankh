package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OutfitSide;
import com.darzee.shankh.enums.OutfitType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoutiqueMeasurementDAO {
    private Long id;
    private Long boutiqueId;
    private OutfitType outfitType;
    private OutfitSide outfitSide;
    private List<String> param;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoutiqueMeasurementDAO(Long boutiqueId, OutfitType outfitType, OutfitSide outfitSide, List<String> param) {
        this.boutiqueId = boutiqueId;
        this.outfitType = outfitType;
        this.outfitSide = outfitSide;
        this.param = param;
    }

    public boolean isBoutiqueIdUpdated(Long value) {
        return value != null && !this.boutiqueId.equals(value);
    }

    public boolean isOutfitTypeUpdated(Integer value) {
        return value != null && !this.outfitType.getOrdinal().equals(value);
    }

    
    


}
