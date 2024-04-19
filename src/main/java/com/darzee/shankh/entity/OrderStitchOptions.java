package com.darzee.shankh.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
@Table(name = "order_stitch_option")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "order-stitch-options-seq", sequenceName = "order_stitch_options_seq", allocationSize = 1)
public class OrderStitchOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-stitch-options-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "stitch_option_id")
    private Long stitchOptionId;

    @Type(type = "jsonb")
    @Column(name = "value", columnDefinition = "jsonb")
    private List<String> values;

    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "is_valid")
    private Boolean isValid;
}
