package com.swd.service;

import com.swd.model.dto.AssetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetService extends BasicService<AssetDTO> {
    Page<AssetDTO> findAllWithSearch(Pageable pageable,
                                     List<String> fields,
                                     String searchBy,
                                     String searchValue);

    AssetDTO updateStatus(Long assetId, Long statusId);

    AssetDTO updateDepartment(Long assetId, Long departmentId);
}
