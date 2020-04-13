package com.swd.service;

import com.swd.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService extends BasicService<UserDTO> {
    Page<UserDTO> findAllWithSearch(Pageable pageable,
                                    List<String> fields,
                                    String searchBy,
                                    String searchValue);

    void updateUserStore(Long managerId, Long storeId);
}
