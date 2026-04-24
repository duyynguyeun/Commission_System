package com.comission.system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "commission_policy")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommissionPolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal companyRate;

    @Column
    private BigDecimal parentRate;

    @Column
    private BigDecimal childRate;

    @Column
    private Boolean isActive;

    @Column
    private Instant effectiveFrom;

    @Column
    private Instant effectiveTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
