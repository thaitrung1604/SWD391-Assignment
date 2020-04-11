package com.swd.service;

import java.util.List;

public interface HelperService<E, D> {
    E updateFields(E e, D d);

    E convertDTOToEntity(D d);

    D convertEntityToDTO(E e);

    D convertEntityToFilteredDTO(E e, List<String> fields);

    E filterFields(E e, List<String> fields);
}
