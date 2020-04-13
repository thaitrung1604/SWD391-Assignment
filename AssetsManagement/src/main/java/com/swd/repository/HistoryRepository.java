package com.swd.repository;

import com.swd.model.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findByAssetId(Long searchValue, Pageable pageable);

    Page<History> findByStoreId(Long searchValue, Pageable pageable);

    Page<History> findByStatusId(Long searchValue, Pageable pageable);

    Page<History> findByUserId(Long searchValue, Pageable pageable);

    Page<History> findByDepartmentId(Long searchValue, Pageable pageable);

    Page<History> findByCreateBy(Long searchValue, Pageable pageable);

    @Query(value = "Select * from history where date between timestamp(:searchValue) " +
            "and timestampadd(DAY,1,:searchValue)", nativeQuery = true)
    Page<History> findByDate(@Param(value = "searchValue") String searchValue, Pageable pageable);

    @Query(value = "Select * from history where expiry_warranty_date between timestamp(:searchValue) " +
            "and timestampadd(DAY,1,:searchValue)", nativeQuery = true)
    Page<History> findByExpiryWarrantyDate(@Param("searchValue") String searchValue, Pageable pageable);

    @Query(value = "Select * from history where next_expiry_warranty_date between timestamp(:searchValue) " +
            "and timestampadd(DAY,1,:searchValue)", nativeQuery = true)
    Page<History> findByNextWarrantyDate(@Param("searchValue") String searchValue, Pageable pageable);
}
