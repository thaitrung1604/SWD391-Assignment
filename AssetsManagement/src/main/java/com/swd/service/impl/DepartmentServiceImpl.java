package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.exception.UniqueConstraintException;
import com.swd.model.dto.DepartmentDTO;
import com.swd.model.entity.Department;
import com.swd.repository.DepartmentRepository;
import com.swd.service.DepartmentService;
import com.swd.service.HelperService;
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
public class DepartmentServiceImpl implements DepartmentService, HelperService<Department, DepartmentDTO> {
    private static final String RESOURCE_NOT_FOUND = "Can't find Department with id: %s ";
    private static final String UNIQUE_CONSTRAINT = "%s is already taken";

    private final DepartmentRepository departmentRepository;
    private final ObjectMapper objectMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ObjectMapper objectMapper) {
        this.departmentRepository = departmentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public DepartmentDTO add(DepartmentDTO departmentDTO) {
        boolean checkName = departmentRepository.existsByName(departmentDTO.getName());
        if (checkName) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getName()));
        }
        boolean checkPhone = departmentRepository.existsByPhone(departmentDTO.getPhone());
        if (checkPhone) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getPhone()));
        }
        boolean checkEmail = departmentRepository.existsByEmail(departmentDTO.getEmail());
        if (checkEmail) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getEmail()));
        }
        return convertEntityToDTO(departmentRepository.save(convertDTOToEntity(departmentDTO)));
    }

    @Override
    public DepartmentDTO find(long id, List<String> fields) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalDepartment.get(), fields);
        }
        return optionalDepartment.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable, List<String> fields) {
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        if (departmentPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return departmentPage.map(department -> convertEntityToFilteredDTO(department, fields));
        }
        return departmentPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<DepartmentDTO> findAllWithSearch(Pageable pageable,
                                                 List<String> fields,
                                                 String searchBy,
                                                 String searchValue) {
        Page<Department> departmentPage;
        switch (searchBy) {
            case "name":
                departmentPage = departmentRepository.findByNameContaining(searchValue, pageable);
                break;
            case "phone":
                departmentPage = departmentRepository.findByPhoneContaining(searchValue, pageable);
                break;
            case "email":
                departmentPage = departmentRepository.findByEmailContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (departmentPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return departmentPage.map(department -> convertEntityToFilteredDTO(department, fields));
        }
        return departmentPage.map(this::convertEntityToDTO);
    }

    @Override
    public DepartmentDTO update(long id, DepartmentDTO departmentDTO) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(department ->
                convertEntityToDTO(departmentRepository.save(updateFields(department, departmentDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            departmentRepository.delete(optionalDepartment.get());
        } else {
            throw new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id));
        }
    }

    @Override
    public Department updateFields(Department department, DepartmentDTO departmentDTO) {
        if (!departmentDTO.getName().equals(department.getName())) {
            boolean checkName = departmentRepository.existsByName(departmentDTO.getName());
            if (checkName) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getName()));
            }
            department.setName(departmentDTO.getName());
        }
        if (!departmentDTO.getEmail().equals(department.getEmail())) {
            boolean checkEmail = departmentRepository.existsByEmail(departmentDTO.getEmail());
            if (checkEmail) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getEmail()));
            }
            department.setEmail(departmentDTO.getName());
        }
        if (!departmentDTO.getPhone().equals(department.getPhone())) {
            boolean checkPhone = departmentRepository.existsByPhone(departmentDTO.getPhone());
            if (checkPhone) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, departmentDTO.getPhone()));
            }
            department.setPhone(departmentDTO.getPhone());
        }
        return department;
    }

    @Override
    public Department convertDTOToEntity(DepartmentDTO departmentDTO) {
        return objectMapper.convertValue(departmentDTO, Department.class);
    }

    @Override
    public DepartmentDTO convertEntityToDTO(Department department) {
        return objectMapper.convertValue(department, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO convertEntityToFilteredDTO(Department department, List<String> fields) {
        return convertEntityToDTO(filterFields(department, fields));
    }

    @Override
    public Department filterFields(Department department, List<String> fields) {
        Department filteredDepartment = new Department();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(department);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(department, filteredDepartment, unwantedFields.toArray(new String[0]));
        return filteredDepartment;
    }
}
