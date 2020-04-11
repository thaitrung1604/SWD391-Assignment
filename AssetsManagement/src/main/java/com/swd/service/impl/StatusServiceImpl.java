package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.StatusDTO;
import com.swd.model.entity.Status;
import com.swd.repository.StatusRepository;
import com.swd.service.StatusService;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final ObjectMapper objectMapper;

    public static final String RESOURCE_NOT_FOUND = "Can't find Status with id: %s ";

    public StatusServiceImpl(StatusRepository statusRepository, ObjectMapper objectMapper) {
        this.statusRepository = statusRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public StatusDTO find(long id) {
        Optional<Status> optionalStatus = statusRepository.findById(id);
        return optionalStatus.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESOURCE_NOT_FOUND, id)));
    }

    @Override
    public List<StatusDTO> findAll() {
        List<Status> statusList = statusRepository.findAll();
        if (statusList.isEmpty()) {
            return Collections.emptyList();
        }
        return statusList.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public StatusDTO convertEntityToDTO(Status status) {
        return objectMapper.convertValue(status, StatusDTO.class);
    }
}
