package com.swd.service;

import java.util.List;

public interface ReadOnlyService<D> {
    D find(long id);

    List<D> findAll();
}
