package com.swd.repository;

import com.swd.model.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Page<Asset> findByNameContaining(String searchValue, Pageable pageable);

    Page<Asset> findByExpiryWarrantyDateContaining(String searchValue, Pageable pageable);

    Page<Asset> findByNextWarrantyDateContaining(String searchValue, Pageable pageable);

    Page<Asset> findByPriceContaining(String searchValue, Pageable pageable);

    Page<Asset> findByPurchaseDateContaining(String searchValue, Pageable pageable);

    Page<Asset> findByDepartmentIdContaining(String searchValue, Pageable pageable);

    Page<Asset> findByManagerIdContaining(String searchValue, Pageable pageable);

    Page<Asset> findByStatusIdContaining(String searchValue, Pageable pageable);

    Page<Asset> findByStoreIdContaining(String searchValue, Pageable pageable);

    Page<Asset> findBySupplierIdContaining(String searchValue, Pageable pageable);

    Page<Asset> findByTypeIdContaining(String searchValue, Pageable pageable);
}
