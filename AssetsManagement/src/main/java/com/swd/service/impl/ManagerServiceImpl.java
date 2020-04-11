package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.ManagerDTO;
import com.swd.model.entity.Account;
import com.swd.model.entity.Manager;
import com.swd.repository.ManagerRepository;
import com.swd.service.HelperService;
import com.swd.service.ManagerService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService, HelperService<Manager, ManagerDTO> {
    private static final String RESOURCE_NOT_FOUND = "Can't find Manager with id: %s ";
    private final ManagerRepository managerRepository;
    private final ObjectMapper objectMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, ObjectMapper objectMapper) {
        this.managerRepository = managerRepository;
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
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
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
    public ManagerDTO update(long id, ManagerDTO managerDTO) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        return optionalManager.map(manager ->
                convertEntityToDTO(managerRepository.save(updateFields(manager, managerDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (optionalManager.isPresent()) {
            managerRepository.delete(optionalManager.get());
        } else {
            throw new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id));
        }
    }

    @Override
    public Manager updateFields(Manager manager, ManagerDTO managerDTO) {
        manager.setName(managerDTO.getName());
        manager.setEmail(managerDTO.getName());
        manager.setPhone(managerDTO.getPhone());
        manager.getStore().setId(managerDTO.getStore().getId());
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
