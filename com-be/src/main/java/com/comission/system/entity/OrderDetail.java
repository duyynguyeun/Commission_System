package com.comission.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal price;

    @Column
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_emp_id")
    private Employee seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_emp_id")
    private Employee parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_link_id")
    private AffiliateLink affiliateLink;
}
