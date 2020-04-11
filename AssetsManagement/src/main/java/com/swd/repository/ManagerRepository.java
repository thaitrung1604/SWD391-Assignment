package com.swd.repository;

import com.swd.model.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Page<Manager> findByNameContaining(String searchValue, Pageable pageable);

    Page<Manager> findByEmailContaining(String searchValue, Pageable pageable);

    Page<Manager> findByPhoneContaining(String searchValue, Pageable pageable);

    Page<Manager> findByStoreId(Long searchValue, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<Manager> findByName(String name);
}
