package com.swd.repository;

import com.swd.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContaining(String searchValue, Pageable pageable);

    Page<User> findByEmailContaining(String searchValue, Pageable pageable);

    Page<User> findByPhoneContaining(String searchValue, Pageable pageable);

    Page<User> findByStoreId(Long searchValue, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<User> findByName(String name);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
