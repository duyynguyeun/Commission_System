package com.comission.system.entity;

import com.comission.system.enums.CommissionEnum;
import com.comission.system.enums.EmployeeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "commission_transaction")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private EmployeeEnum commissionRole;

    @Column
    private BigDecimal commissionRate;

    @Column
    private BigDecimal commissionAmount;

    @Enumerated(EnumType.STRING)
    @Column
    private CommissionEnum status;

    @Column
    private Instant createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private CommissionPolicy commissionPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
