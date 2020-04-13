package com.swd.repository;

import com.swd.model.entity.MinuteOfHandover;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinuteOfHandoverRepository extends JpaRepository<MinuteOfHandover, Long> {
    Page<MinuteOfHandover> findByCurrentUserIdContaining(String searchValue, Pageable pageable);

    Page<MinuteOfHandover> findByPreviousUserIdContaining(String searchValue, Pageable pageable);

    Page<MinuteOfHandover> findByAssetIdContaining(String searchValue, Pageable pageable);

    Page<MinuteOfHandover> findByCreateByContaining(String searchValue, Pageable pageable);

    Page<MinuteOfHandover> findByDateContaining(String searchValue, Pageable pageable);
}
