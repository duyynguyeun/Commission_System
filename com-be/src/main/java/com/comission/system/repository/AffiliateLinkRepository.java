package com.comission.system.repository;

import com.comission.system.entity.AffiliateLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateLinkRepository extends JpaRepository<AffiliateLink, Long> {
    boolean existsByAffCode(String affCode);
    Optional<AffiliateLink> findByAffCode(String affCode);
    Optional<AffiliateLink> findByEmployee_IdAndProduct_Id(Long employeeId, Long productId);
    List<AffiliateLink> findByEmployee_Id(Long employeeId);
    boolean existsByEmployee_Id(Long employeeId);
    boolean existsByProduct_Id(Long productId);
}
