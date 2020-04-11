package com.swd.service;

import com.swd.model.dto.HistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService extends ReadOnlyAdvanceService<HistoryDTO> {
    Page<HistoryDTO> findAll(Pageable pageable);
}
