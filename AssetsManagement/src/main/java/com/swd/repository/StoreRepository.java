package com.swd.repository;

import com.swd.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findByNameContaining(String searchValue, Pageable pageable);

    Page<Store> findByAddressContaining(String searchValue, Pageable pageable);

    Page<Store> findByPhoneContaining(String searchValue, Pageable pageable);
}
