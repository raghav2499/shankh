package com.darzee.shankh.dao;

import com.darzee.shankh.enums.BoutiqueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.bouncycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi.sha256WithSM2;

@Getter
@Setter
@NoArgsConstructor
public class BoutiqueDAO {

    private Long id;
    private String boutiqueReferenceId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoutiqueType boutiqueType;
    private Integer tailorCount;
    private TailorDAO adminTailor;

    public BoutiqueDAO(String boutiqueName, String boutiqueType, String boutiqueReferenceId) {
        this.name = boutiqueName;
        this.boutiqueType = BoutiqueType.getEnumMap().get(boutiqueType);
        this.boutiqueReferenceId = boutiqueReferenceId;
    }

    public boolean isNameUpdated(String value) {
        return value != null && !this.name.equals(value);
    }

    public boolean isBoutiqueTypeUpdated(Integer boutiqueOrdinal) {
        return boutiqueOrdinal != null && !this.boutiqueType.getOrdinal().equals(boutiqueOrdinal);
    }
    public boolean isTailorCountUpdated(Integer value) {
        return value != null && value != this.tailorCount;
    }

}
