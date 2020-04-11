package com.swd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReadOnlyAdvanceService<D> extends ReadOnlyService<D> {
    Page<D> findAllWithSearch(Pageable pageable,
                              String searchBy,
                              String searchValue);
}