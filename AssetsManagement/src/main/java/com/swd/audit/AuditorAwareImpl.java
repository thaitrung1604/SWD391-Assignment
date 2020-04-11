package com.swd.audit;

import com.swd.model.entity.Manager;
import com.swd.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Optional<Manager> optionalManager = managerRepository.findByName((String) authentication.getPrincipal());
        Long result = null;
        if (optionalManager.isPresent()) {
            result = optionalManager.get().getId();
        }
        return Optional.ofNullable(result);
    }
}