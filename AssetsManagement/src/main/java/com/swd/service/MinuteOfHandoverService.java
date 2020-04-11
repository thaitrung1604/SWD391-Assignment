package com.swd.service;

import com.swd.model.dto.MinuteOfHandoverDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MinuteOfHandoverService {
    MinuteOfHandoverDTO add(MinuteOfHandoverDTO dto);

    MinuteOfHandoverDTO find(long id);

    Page<MinuteOfHandoverDTO> findAll(Pageable pageable);

    Page<MinuteOfHandoverDTO> findAllWithSearch(Pageable pageable, String searchBy, String searchValue);

    MinuteOfHandoverDTO update(long id, MinuteOfHandoverDTO dto);

    void delete(long id);
}
