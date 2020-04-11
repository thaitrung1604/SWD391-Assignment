package com.swd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BasicService<D> {
    D add(D dto);

    D find(long id, List<String> fields);

    Page<D> findAll(Pageable pageable, List<String> fields);

    D update(long id, D dto);

    void delete(long id);
}
