package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.exception.UniqueConstraintException;
import com.swd.model.dto.ManagerDTO;
import com.swd.model.entity.Account;
import com.swd.model.entity.Manager;
import com.swd.model.entity.Store;
import com.swd.repository.AccountRepository;
import com.swd.repository.ManagerRepository;
import com.swd.repository.StoreRepository;
import com.swd.service.HelperService;
import com.swd.service.ManagerService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService, HelperService<Manager, ManagerDTO> {
    private static final String MANAGER_NOT_FOUND = "Can't find Manager with id: %s ";
    private static final String STORE_NOT_FOUND = "Can't find Store with id %s";
    private static final String UNIQUE_CONSTRAINT = "%s is already taken";
    private final ManagerRepository managerRepository;
    private final StoreRepository storeRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository,
                              StoreRepository storeRepository,
                              AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.managerRepository = managerRepository;
        this.storeRepository = storeRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public ManagerDTO add(ManagerDTO managerDTO) {
        Manager manager = convertDTOToEntity(managerDTO);
        Account account = objectMapper.convertValue(managerDTO.getAccount(), Account.class);
        account.setManager(manager);
        manager.setAccount(account);
        return convertEntityToDTO(managerRepository.save(manager));
    }

    @Override
    public ManagerDTO find(long id, List<String> fields) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (optionalManager.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalManager.get(), fields);
        }
        return optionalManager.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id)));
    }

    @Override
    public Page<ManagerDTO> findAll(Pageable pageable, List<String> fields) {
        Page<Manager> managerPage = managerRepository.findAll(pageable);
        if (managerPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return managerPage.map(manager -> convertEntityToFilteredDTO(manager, fields));
        }
        return managerPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<ManagerDTO> findAllWithSearch(Pageable pageable,
                                              List<String> fields,
                                              String searchBy,
                                              String searchValue) {
        Page<Manager> managerPage;
        switch (searchBy) {
            case "name":
                managerPage = managerRepository.findByNameContaining(searchValue, pageable);
                break;
            case "phone":
                managerPage = managerRepository.findByPhoneContaining(searchValue, pageable);
                break;
            case "email":
                managerPage = managerRepository.findByEmailContaining(searchValue, pageable);
                break;
            case "store_id":
                managerPage = managerRepository.findByStoreId(Long.parseLong(searchValue), pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (managerPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return managerPage.map(manager -> convertEntityToFilteredDTO(manager, fields));
        }
        return managerPage.map(this::convertEntityToDTO);
    }

    @Override
    public void updateManagerStore(Long managerId, Long storeId) {
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (!optionalManager.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, managerId));
        }
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (!optionalStore.isPresent()) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, managerId));
        }
        Manager manager = optionalManager.get();
        manager.setStore(optionalStore.get());
        managerRepository.save(manager);
    }

    @Override
    public ManagerDTO update(long id, ManagerDTO managerDTO) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        return optionalManager.map(manager ->
                convertEntityToDTO(managerRepository.save(updateFields(manager, managerDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (optionalManager.isPresent()) {
            managerRepository.delete(optionalManager.get());
        } else {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id));
        }
    }

    @Override
    public Manager updateFields(Manager manager, ManagerDTO managerDTO) {
        if (!managerDTO.getAccount().getUsername().equals(manager.getAccount().getUsername())) {
            boolean checkUsername = accountRepository.existsByUsername(managerDTO.getAccount().getUsername());
            if (checkUsername) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getAccount().getUsername()));
            }
            manager.getAccount().setUsername(managerDTO.getAccount().getUsername());
        }
        if (!managerDTO.getEmail().equals(manager.getEmail())) {
            boolean checkEmail = managerRepository.existsByEmail(managerDTO.getEmail());
            if (checkEmail) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getEmail()));
            }
            manager.setEmail(managerDTO.getEmail());
        }
        if (!managerDTO.getPhone().equals(manager.getPhone())) {
            boolean checkPhone = managerRepository.existsByPhone(managerDTO.getPhone());
            if (checkPhone) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, managerDTO.getPhone()));
            }
            manager.setPhone(managerDTO.getPhone());
        }
        manager.setName(managerDTO.getName());
        manager.getAccount().setPassword(passwordEncoder.encode(managerDTO.getAccount().getPassword()));
        return manager;
    }

    @Override
    public Manager convertDTOToEntity(ManagerDTO managerDTO) {
        return objectMapper.convertValue(managerDTO, Manager.class);
    }

    @Override
    public ManagerDTO convertEntityToDTO(Manager manager) {
        return objectMapper.convertValue(manager, ManagerDTO.class);
    }

    @Override
    public ManagerDTO convertEntityToFilteredDTO(Manager manager, List<String> fields) {
        return convertEntityToDTO(filterFields(manager, fields));
    }

    @Override
    public Manager filterFields(Manager manager, List<String> fields) {
        Manager filteredManager = new Manager();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(manager);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(manager, filteredManager, unwantedFields.toArray(new String[0]));
        return filteredManager;
    }
}
