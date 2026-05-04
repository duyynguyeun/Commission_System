package com.comission.system.repository;

import com.comission.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByParentId(Long parentId);
    Optional<Employee> findByAccount_Id(Long accountId);
}
