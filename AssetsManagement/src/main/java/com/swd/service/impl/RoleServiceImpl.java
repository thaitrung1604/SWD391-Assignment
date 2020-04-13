package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.RoleDTO;
import com.swd.model.entity.Role;
import com.swd.repository.RoleRepository;
import com.swd.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    public static final String RESOURCE_NOT_FOUND = "Can't find Role with id: %s ";

    public RoleServiceImpl(RoleRepository roleRepository, ObjectMapper objectMapper) {
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public RoleDTO find(long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            return Collections.emptyList();
        }
        return roleList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public RoleDTO convertEntityToDTO(Role role) {
        return objectMapper.convertValue(role, RoleDTO.class);
    }
}
