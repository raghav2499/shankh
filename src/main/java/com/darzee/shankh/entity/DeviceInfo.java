package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "device_info")
@SequenceGenerator(name = "device-info-seq", sequenceName = "device_info_seq", allocationSize = 1)
@Entity
@Getter
@Setter
public class DeviceInfo extends GenericEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device-info-seq")
    private Long id;

    @Column(name = "boutique_id", nullable = false)
    private Long boutiqueId;

    @Column(name = "tailor_id", nullable = false)
    private Long tailorId;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "app_version")
    private String appVersion;

}
