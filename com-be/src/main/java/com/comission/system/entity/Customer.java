package com.comission.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String fullName;

    @Column
    private LocalDate dob;

    @Column
    private String address;
}
