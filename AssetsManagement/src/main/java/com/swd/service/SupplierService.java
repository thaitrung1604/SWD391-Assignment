package com.swd.service;

import com.swd.model.dto.SupplierDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService extends BasicService<SupplierDTO> {
    Page<SupplierDTO> findAllWithSearch(Pageable pageable, List<String> fields, String searchBy, String searchValue);
}
