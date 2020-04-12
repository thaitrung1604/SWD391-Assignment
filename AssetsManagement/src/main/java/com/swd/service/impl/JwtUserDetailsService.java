package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.UniqueConstraintException;
import com.swd.model.dto.ManagerDTO;
import com.swd.model.entity.Account;
import com.swd.model.entity.Manager;
import com.swd.repository.AccountRepository;
import com.swd.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final String UNIQUE_CONSTRAINT = "%s is already taken";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found with username: " + username);
        }
        return new User(optionalAccount.get().getUsername(), optionalAccount.get().getPassword(), new ArrayList<>());
    }

    public ManagerDTO save(ManagerDTO managerDTO) {
        boolean checkUsername = accountRepository.existsByUsername(managerDTO.getAccount().getUsername());
        if (checkUsername) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getAccount().getUsername()));
        }
        boolean checkEmail = managerRepository.existsByEmail(managerDTO.getEmail());
        if (checkEmail) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getEmail()));
        }
        boolean checkPhone = managerRepository.existsByPhone(managerDTO.getPhone());
        if (checkPhone) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getPhone()));
        }
        Manager manager = objectMapper.convertValue(managerDTO, Manager.class);
        Account account = objectMapper.convertValue(managerDTO.getAccount(), Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setManager(manager);
        manager.setAccount(account);
        return objectMapper.convertValue(managerRepository.saveAndFlush(manager), ManagerDTO.class);
    }
}
