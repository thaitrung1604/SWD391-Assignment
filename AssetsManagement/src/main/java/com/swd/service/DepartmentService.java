package com.swd.service;

import com.swd.model.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService extends BasicService<DepartmentDTO> {
    Page<DepartmentDTO> findAllWithSearch(Pageable pageable,
                                          List<String> fields,
                                          String searchBy,
                                          String searchValue);
}
