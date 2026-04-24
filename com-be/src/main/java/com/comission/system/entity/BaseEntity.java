package com.comission.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {
    @Column(name = "create_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updatedAt;
}
