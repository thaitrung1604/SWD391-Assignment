package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.HistoryDTO;
import com.swd.model.entity.History;
import com.swd.repository.HistoryRepository;
import com.swd.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {
    private static final String HISTORY_NOT_FOUND = "Can't find history with id %s";
    private final HistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    public HistoryServiceImpl(HistoryRepository historyRepository, ObjectMapper objectMapper) {
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public HistoryDTO find(long id) {
        Optional<History> optionalHistory = historyRepository.findById(id);
        if (!optionalHistory.isPresent()) {
            throw new ResourceNotFoundException(String.format(HISTORY_NOT_FOUND, id));
        }
        return convertEntityToDTO(optionalHistory.get());
    }

    @Override
    public List<HistoryDTO> findAll() {
        return null;
    }

    public Page<HistoryDTO> findAll(Pageable pageable) {
        Page<History> historyPage = historyRepository.findAll(pageable);
        if (historyPage.isEmpty()) {
            return Page.empty();
        }
        return historyPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<HistoryDTO> findAllWithSearch(Pageable pageable, String searchBy, String searchValue) {
        Page<History> historyPage;
        switch (searchBy) {
            case "asset_id":
                historyPage = historyRepository.findByAssetIdContaining(searchValue, pageable);
                break;
            case "store_id":
                historyPage = historyRepository.findByStoreIdContaining(searchValue, pageable);
                break;
            case "status_id":
                historyPage = historyRepository.findByStatusIdContaining(searchValue, pageable);
                break;
            case "manager_id":
                historyPage = historyRepository.findByManagerIdContaining(searchValue, pageable);
                break;
            case "department_id":
                historyPage = historyRepository.findByDepartmentIdContaining(searchValue, pageable);
                break;
            case "create_by":
                historyPage = historyRepository.findByCreateByContaining(searchValue, pageable);
                break;
            case "date":
                historyPage = historyRepository.findByDateContaining(searchValue, pageable);
                break;
            case "next_warranty_date":
                historyPage = historyRepository.findByNextWarrantyDateContaining(searchValue, pageable);
                break;
            case "expiry_warranty_date":
                historyPage = historyRepository.findByExpiryWarrantyDateContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (historyPage.isEmpty()) {
            return Page.empty();
        }
        return historyPage.map(this::convertEntityToDTO);
    }

    public HistoryDTO convertEntityToDTO(History history) {
        HistoryDTO historyDTO = objectMapper.convertValue(history, HistoryDTO.class);
        historyDTO.setAssetId(history.getAsset().getId());
        return historyDTO;
    }
}
