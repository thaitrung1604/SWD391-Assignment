package com.swd.repository;

import com.swd.model.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findByNameContaining(String searchValue, Pageable pageable);

    Page<Department> findByEmailContaining(String searchValue, Pageable pageable);

    Page<Department> findByPhoneContaining(String searchValue, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
