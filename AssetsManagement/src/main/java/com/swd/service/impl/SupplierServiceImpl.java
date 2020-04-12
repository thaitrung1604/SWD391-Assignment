package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.exception.UniqueConstraintException;
import com.swd.model.dto.SupplierDTO;
import com.swd.model.entity.Supplier;
import com.swd.repository.SupplierRepository;
import com.swd.service.HelperService;
import com.swd.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService, HelperService<Supplier, SupplierDTO> {
    private static final String RESOURCE_NOT_FOUND = "Can't find Supplier with id: %s ";
    private static final String UNIQUE_CONSTRAINT = "%s is already taken";
    private final SupplierRepository supplierRepository;
    private final ObjectMapper objectMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, ObjectMapper objectMapper) {
        this.supplierRepository = supplierRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public SupplierDTO add(SupplierDTO supplierDTO) {
        boolean checkName = supplierRepository.existsByName(supplierDTO.getName());
        if (checkName) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getName()));
        }
        boolean checkPhone = supplierRepository.existsByPhone(supplierDTO.getPhone());
        if (checkPhone) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getPhone()));
        }
        boolean checkEmail = supplierRepository.existsByEmail(supplierDTO.getEmail());
        if (checkEmail) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getEmail()));
        }
        return convertEntityToDTO(supplierRepository.save(convertDTOToEntity(supplierDTO)));
    }

    @Override
    public SupplierDTO find(long id, List<String> fields) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalSupplier.get(), fields);
        }
        return optionalSupplier.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public Page<SupplierDTO> findAll(Pageable pageable, List<String> fields) {
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);
        if (supplierPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return supplierPage.map(supplier -> convertEntityToFilteredDTO(supplier, fields));
        }
        return supplierPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<SupplierDTO> findAllWithSearch(Pageable pageable,
                                               List<String> fields,
                                               String searchBy,
                                               String searchValue) {
        Page<Supplier> supplierPage;
        switch (searchBy) {
            case "name":
                supplierPage = supplierRepository.findByNameContaining(searchValue, pageable);
                break;
            case "phone":
                supplierPage = supplierRepository.findByPhoneContaining(searchValue, pageable);
                break;
            case "email":
                supplierPage = supplierRepository.findByEmailContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (supplierPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return supplierPage.map(supplier -> convertEntityToFilteredDTO(supplier, fields));
        }
        return supplierPage.map(this::convertEntityToDTO);
    }

    @Override
    public SupplierDTO update(long id, SupplierDTO supplierDTO) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        return optionalSupplier.map(supplier ->
                convertEntityToDTO(supplierRepository.save(updateFields(supplier, supplierDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isPresent()) {
            supplierRepository.delete(optionalSupplier.get());
        } else {
            throw new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id));
        }
    }

    @Override
    public Supplier updateFields(Supplier supplier, SupplierDTO supplierDTO) {
        if (!supplierDTO.getName().equals(supplier.getName())) {
            boolean checkName = supplierRepository.existsByName(supplierDTO.getName());
            if (checkName) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getName()));
            }
            supplier.setName(supplierDTO.getName());
        }
        if (!supplierDTO.getEmail().equals(supplier.getEmail())) {
            boolean checkEmail = supplierRepository.existsByEmail(supplierDTO.getEmail());
            if (checkEmail) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getEmail()));
            }
            supplier.setEmail(supplierDTO.getName());
        }
        if (!supplierDTO.getPhone().equals(supplier.getPhone())) {
            boolean checkPhone = supplierRepository.existsByPhone(supplierDTO.getPhone());
            if (checkPhone) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, supplierDTO.getPhone()));
            }
            supplier.setPhone(supplierDTO.getPhone());
        }
        return supplier;
    }

    @Override
    public Supplier convertDTOToEntity(SupplierDTO supplierDTO) {
        return objectMapper.convertValue(supplierDTO, Supplier.class);
    }

    @Override
    public SupplierDTO convertEntityToDTO(Supplier supplier) {
        return objectMapper.convertValue(supplier, SupplierDTO.class);
    }

    @Override
    public SupplierDTO convertEntityToFilteredDTO(Supplier supplier, List<String> fields) {
        return convertEntityToDTO(filterFields(supplier, fields));
    }

    @Override
    public Supplier filterFields(Supplier supplier, List<String> fields) {
        Supplier filteredSupplier = new Supplier();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(supplier);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(supplier, filteredSupplier, unwantedFields.toArray(new String[0]));
        return filteredSupplier;
    }
}
