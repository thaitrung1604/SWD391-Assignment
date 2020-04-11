package com.swd.service;

import com.swd.model.dto.StoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService extends BasicService<StoreDTO> {
    Page<StoreDTO> findAllWithSearch(Pageable pageable,
                                     List<String> fields,
                                     String searchBy,
                                     String searchValue);
}
