package com.darzee.shankh.entity;

import com.darzee.shankh.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "customer")
@Entity
@Getter
@Setter
@SequenceGenerator(name = "customer-seq", sequenceName = "customer_seq", allocationSize = 1)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer-seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Measurements> measurements;

}
