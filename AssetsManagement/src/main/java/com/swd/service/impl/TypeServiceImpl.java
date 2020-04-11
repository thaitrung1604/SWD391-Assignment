package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.TypeDTO;
import com.swd.model.entity.Type;
import com.swd.repository.TypeRepository;
import com.swd.service.TypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final ObjectMapper objectMapper;

    public static final String RESOURCE_NOT_FOUND = "Can't find Type with id: %s ";

    public TypeServiceImpl(TypeRepository typeRepository, ObjectMapper objectMapper) {
        this.typeRepository = typeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public TypeDTO find(long id) {
        Optional<Type> optionalType = typeRepository.findById(id);
        return optionalType.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public List<TypeDTO> findAll() {
        List<Type> typeList = typeRepository.findAll();
        if (typeList.isEmpty()) {
            return Collections.emptyList();
        }
        return typeList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public TypeDTO convertEntityToDTO(Type type) {
        return objectMapper.convertValue(type, TypeDTO.class);
    }
}
