package com.darzee.shankh.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeviceInfoDAO {
    private Long id;

    private Long boutiqueId;

    private Long tailorId;

    private String deviceToken;

    private String appVersion;
}
