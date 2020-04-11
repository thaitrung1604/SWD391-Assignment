package com.swd.repository;

import com.swd.model.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findByAssetIdContaining(String searchValue, Pageable pageable);

    Page<History> findByStoreIdContaining(String searchValue, Pageable pageable);

    Page<History> findByStatusIdContaining(String searchValue, Pageable pageable);

    Page<History> findByManagerIdContaining(String searchValue, Pageable pageable);

    Page<History> findByDepartmentIdContaining(String searchValue, Pageable pageable);

    Page<History> findByCreateByContaining(String searchValue, Pageable pageable);

    Page<History> findByDateContaining(String searchValue, Pageable pageable);

    Page<History> findByExpiryWarrantyDateContaining(String searchValue, Pageable pageable);

    Page<History> findByNextWarrantyDateContaining(String searchValue, Pageable pageable);
}
