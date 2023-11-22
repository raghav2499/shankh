package com.darzee.shankh.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Table(name = "measurements")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "measurements-seq", sequenceName = "measurements_seq", allocationSize = 1)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Measurements extends GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurements-seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "measurement_revision_id")
    private Long measurementRevisionId;
}