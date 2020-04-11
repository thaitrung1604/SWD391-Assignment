package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.StoreDTO;
import com.swd.model.entity.Store;
import com.swd.repository.StoreRepository;
import com.swd.service.HelperService;
import com.swd.service.StoreService;
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
public class StoreServiceImpl implements StoreService, HelperService<Store, StoreDTO> {
    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    private static final String RESOURCE_NOT_FOUND = "Can't find Store with id: %s ";

    public StoreServiceImpl(StoreRepository storeRepository, ObjectMapper objectMapper) {
        this.storeRepository = storeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public StoreDTO add(StoreDTO storeDTO) {
        return convertEntityToDTO(storeRepository.save(convertDTOToEntity(storeDTO)));
    }

    @Override
    public StoreDTO find(long id, List<String> fields) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalStore.get(), fields);
        }
        return optionalStore.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public Page<StoreDTO> findAll(Pageable pageable, List<String> fields) {
        Page<Store> storePage = storeRepository.findAll(pageable);
        if (storePage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return storePage.map(store -> convertEntityToFilteredDTO(store, fields));
        }
        return storePage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<StoreDTO> findAllWithSearch(Pageable pageable,
                                            List<String> fields,
                                            String searchBy,
                                            String searchValue) {
        Page<Store> storePage;
        switch (searchBy) {
            case "name":
                storePage = storeRepository.findByNameContaining(searchValue, pageable);
                break;
            case "phone":
                storePage = storeRepository.findByPhoneContaining(searchValue, pageable);
                break;
            case "address":
                storePage = storeRepository.findByAddressContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (storePage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return storePage.map(store -> convertEntityToFilteredDTO(store, fields));
        }
        return storePage.map(this::convertEntityToDTO);
    }

    @Override
    public StoreDTO update(long id, StoreDTO storeDTO) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        return optionalStore.map(store ->
                convertEntityToDTO(storeRepository.save(updateFields(store, storeDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isPresent()) {
            storeRepository.delete(optionalStore.get());
        } else {
            throw new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id));
        }
    }

    @Override
    public Store updateFields(Store store, StoreDTO storeDTO) {
        store.setName(storeDTO.getName());
        store.setAddress(storeDTO.getAddress());
        store.setPhone(storeDTO.getPhone());
        return store;
    }

    @Override
    public Store convertDTOToEntity(StoreDTO storeDTO) {
        return objectMapper.convertValue(storeDTO, Store.class);
    }

    @Override
    public StoreDTO convertEntityToDTO(Store store) {
        return objectMapper.convertValue(store, StoreDTO.class);
    }

    @Override
    public StoreDTO convertEntityToFilteredDTO(Store store, List<String> fields) {
        return convertEntityToDTO(filterFields(store, fields));
    }

    @Override
    public Store filterFields(Store store, List<String> fields) {
        Store filteredStore = new Store();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(store);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(store, filteredStore, unwantedFields.toArray(new String[0]));
        return filteredStore;
    }
}
