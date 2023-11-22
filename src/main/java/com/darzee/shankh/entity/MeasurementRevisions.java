//package com.darzee.shankh.entity;
//
//import com.darzee.shankh.enums.OutfitType;
//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.TypeDef;
//
//import javax.persistence.*;
//import java.util.Map;
//
//@Table(name = "measurement_revisions")
//@Entity
//@Getter
//@Setter
//@SequenceGenerator(name = "measurement-revisions-seq", sequenceName = "measurements_revisions_seq", allocationSize = 1)
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//public class MeasurementRevisions {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurement-revisions-seq")
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @Type(type = "jsonb")
//    @Column(name = "measurement_value", columnDefinition = "jsonb")
//    private Map<String, Double> measurementValue;
//
//    @Column(name = "outfit_type")
//    @Enumerated(EnumType.ORDINAL)
//    private OutfitType outfitType;
//
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//}
