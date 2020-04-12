package com.swd.service;

import com.swd.model.dto.ManagerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManagerService extends BasicService<ManagerDTO> {
    Page<ManagerDTO> findAllWithSearch(Pageable pageable,
                                       List<String> fields,
                                       String searchBy,
                                       String searchValue);

    void updateManagerStore(Long managerId, Long storeId);
}
