package com.swd.repository;

import com.swd.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Page<Supplier> findByNameContaining(String searchValue, Pageable pageable);

    Page<Supplier> findByEmailContaining(String searchValue, Pageable pageable);

    Page<Supplier> findByPhoneContaining(String searchValue, Pageable pageable);
}
