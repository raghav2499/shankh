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
}
