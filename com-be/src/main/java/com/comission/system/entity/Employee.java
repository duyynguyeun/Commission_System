package com.comission.system.entity;

import com.comission.system.enums.EmployeeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Employee extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long parentId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String fullName;

    @Column
    @Enumerated(EnumType.STRING)
    private EmployeeEnum role;
}