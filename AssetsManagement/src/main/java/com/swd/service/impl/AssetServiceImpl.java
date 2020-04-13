package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.AssetDTO;
import com.swd.model.entity.Asset;
import com.swd.model.entity.Department;
import com.swd.model.entity.History;
import com.swd.model.entity.Status;
import com.swd.repository.AssetRepository;
import com.swd.repository.DepartmentRepository;
import com.swd.repository.HistoryRepository;
import com.swd.repository.StatusRepository;
import com.swd.service.AssetService;
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
public class AssetServiceImpl implements AssetService, HelperService<Asset, AssetDTO> {
    private static final String ASSET_NOT_FOUND = "Can't find Asset with id: %s ";
    private static final String STATUS_NOT_FOUND = "Can't find Status with id: %s";
    private static final String DEPARTMENT_NOT_FOUND = "Can't find Department with id: %s";
    private final ObjectMapper objectMapper;
    private final AssetRepository assetRepository;
    private final HistoryRepository historyRepository;
    private final DepartmentRepository departmentRepository;
    private final StatusRepository statusRepository;

    public AssetServiceImpl(ObjectMapper objectMapper,
                            AssetRepository assetRepository,
                            HistoryRepository historyRepository,
                            DepartmentRepository departmentRepository,
                            StatusRepository statusRepository) {
        this.objectMapper = objectMapper;
        this.assetRepository = assetRepository;
        this.historyRepository = historyRepository;
        this.departmentRepository = departmentRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public AssetDTO add(AssetDTO assetDTO) {
        Asset returnAsset = assetRepository.save(convertDTOToEntity(assetDTO));
        History history = new History();
        history.setAsset(returnAsset);
        history.setDepartmentId(returnAsset.getDepartment().getId());
        history.setManagerId(returnAsset.getUser().getId());
        history.setStatusId(returnAsset.getStatus().getId());
        history.setStoreId(returnAsset.getStore().getId());
        history.setExpiryWarrantyDate(returnAsset.getExpiryWarrantyDate());
        history.setNextWarrantyDate(returnAsset.getNextWarrantyDate());
        historyRepository.save(history);
        return convertEntityToDTO(returnAsset);
    }

    @Override
    public AssetDTO find(long id, List<String> fields) {
        Optional<Asset> optionalAsset = assetRepository.findById(id);
        if (optionalAsset.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalAsset.get(), fields);
        }
        return optionalAsset.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, id)));
    }

    @Override
    public Page<AssetDTO> findAll(Pageable pageable, List<String> fields) {
        Page<Asset> assetPage = assetRepository.findAll(pageable);
        if (assetPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return assetPage.map(asset -> convertEntityToFilteredDTO(asset, fields));
        }
        return assetPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<AssetDTO> findAllWithSearch(Pageable pageable,
                                            List<String> fields,
                                            String searchBy,
                                            String searchValue) {
        Page<Asset> assetPage;
        switch (searchBy) {
            case "name":
                assetPage = assetRepository.findByNameContaining(searchValue, pageable);
                break;
            case "expiry_warranty_date":
                assetPage = assetRepository.findByExpiryWarrantyDateContaining(searchValue, pageable);
                break;
            case "next_warranty_date":
                assetPage = assetRepository.findByNextWarrantyDateContaining(searchValue, pageable);
                break;
            case "price":
                assetPage = assetRepository.findByPriceContaining(searchValue, pageable);
                break;
            case "purchase_date":
                assetPage = assetRepository.findByPurchaseDateContaining(searchValue, pageable);
                break;
            case "department_id":
                assetPage = assetRepository.findByDepartmentIdContaining(searchValue, pageable);
                break;
            case "manager_id":
                assetPage = assetRepository.findByUserIdContaining(searchValue, pageable);
                break;
            case "status_id":
                assetPage = assetRepository.findByStatusIdContaining(searchValue, pageable);
                break;
            case "store_id":
                assetPage = assetRepository.findByStoreIdContaining(searchValue, pageable);
                break;
            case "supplier_id":
                assetPage = assetRepository.findBySupplierIdContaining(searchValue, pageable);
                break;
            case "type_id":
                assetPage = assetRepository.findByTypeIdContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (assetPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return assetPage.map(asset -> convertEntityToFilteredDTO(asset, fields));
        }
        return assetPage.map(this::convertEntityToDTO);
    }

    @Override
    public AssetDTO updateStatus(Long assetId, Long statusId) {
        Optional<Status> optionalStatus = statusRepository.findById(statusId);
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        if (!optionalStatus.isPresent()) {
            throw new ResourceNotFoundException(String.format(STATUS_NOT_FOUND, statusId));
        }
        if (optionalAsset.isPresent()) {
            Asset returnAsset = optionalAsset.get();
            returnAsset.setStatus(optionalStatus.get());
            History history = new History();
            history.setAsset(returnAsset);
            history.setDepartmentId(returnAsset.getDepartment().getId());
            history.setManagerId(returnAsset.getUser().getId());
            history.setStatusId(returnAsset.getStatus().getId());
            history.setStoreId(returnAsset.getStore().getId());
            history.setExpiryWarrantyDate(returnAsset.getExpiryWarrantyDate());
            history.setNextWarrantyDate(returnAsset.getNextWarrantyDate());
            historyRepository.save(history);
            return convertEntityToDTO(assetRepository.save(returnAsset));
        }
        throw new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, assetId));
    }

    @Override
    public AssetDTO updateDepartment(Long assetId, Long departmentId) {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if (!optionalDepartment.isPresent()) {
            throw new ResourceNotFoundException(String.format(DEPARTMENT_NOT_FOUND, departmentId));
        }
        if (optionalAsset.isPresent()) {
            Asset returnAsset = optionalAsset.get();
            returnAsset.setDepartment(optionalDepartment.get());
            History history = new History();
            history.setAsset(returnAsset);
            history.setDepartmentId(returnAsset.getDepartment().getId());
            history.setManagerId(returnAsset.getUser().getId());
            history.setStatusId(returnAsset.getStatus().getId());
            history.setStoreId(returnAsset.getStore().getId());
            history.setExpiryWarrantyDate(returnAsset.getExpiryWarrantyDate());
            history.setNextWarrantyDate(returnAsset.getNextWarrantyDate());
            historyRepository.save(history);
            return convertEntityToDTO(assetRepository.save(returnAsset));
        }
        throw new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, assetId));
    }

    @Override
    public AssetDTO update(long id, AssetDTO assetDTO) {
        Optional<Asset> optionalAsset = assetRepository.findById(id);
        return optionalAsset.map(asset ->
                convertEntityToDTO(assetRepository.save(updateFields(asset, assetDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<Asset> optionalAsset = assetRepository.findById(id);
        if (optionalAsset.isPresent()) {
            assetRepository.delete(optionalAsset.get());
        } else {
            throw new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, id));
        }
    }

    @Override
    public Asset updateFields(Asset asset, AssetDTO assetDTO) {
        asset.setDescription(assetDTO.getDescription());
        asset.setExpiryWarrantyDate(assetDTO.getExpiryWarrantyDate());
        asset.setName(assetDTO.getName());
        asset.setNextWarrantyDate(assetDTO.getNextWarrantyDate());
        asset.setPrice(assetDTO.getPrice());
        asset.setPurchaseDate(assetDTO.getPurchaseDate());
        History history = new History();
        history.setAsset(asset);
        history.setDepartmentId(asset.getDepartment().getId());
        history.setManagerId(asset.getUser().getId());
        history.setStatusId(asset.getStatus().getId());
        history.setStoreId(asset.getStore().getId());
        history.setExpiryWarrantyDate(asset.getExpiryWarrantyDate());
        history.setNextWarrantyDate(asset.getNextWarrantyDate());
        historyRepository.save(history);
        return asset;
    }

    @Override
    public Asset convertDTOToEntity(AssetDTO assetDTO) {
        return objectMapper.convertValue(assetDTO, Asset.class);
    }

    @Override
    public AssetDTO convertEntityToDTO(Asset asset) {
        return objectMapper.convertValue(asset, AssetDTO.class);
    }

    @Override
    public AssetDTO convertEntityToFilteredDTO(Asset asset, List<String> fields) {
        return convertEntityToDTO(filterFields(asset, fields));
    }

    @Override
    public Asset filterFields(Asset asset, List<String> fields) {
        Asset filteredAsset = new Asset();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(asset);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(asset, filteredAsset, unwantedFields.toArray(new String[0]));
        return filteredAsset;
    }
}
