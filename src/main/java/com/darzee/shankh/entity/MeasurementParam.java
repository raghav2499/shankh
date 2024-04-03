package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "measurement_param")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "measurement-param-seq", sequenceName = "measurement_param_seq", allocationSize = 1)
public class MeasurementParam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurement-param-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
