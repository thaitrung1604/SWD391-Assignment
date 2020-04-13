package com.swd.service.impl;

import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.model.dto.MinuteOfHandoverDTO;
import com.swd.model.entity.Asset;
import com.swd.model.entity.History;
import com.swd.model.entity.User;
import com.swd.model.entity.MinuteOfHandover;
import com.swd.repository.AssetRepository;
import com.swd.repository.HistoryRepository;
import com.swd.repository.UserRepository;
import com.swd.repository.MinuteOfHandoverRepository;
import com.swd.service.MinuteOfHandoverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MinuteOfHandoverServiceImpl implements MinuteOfHandoverService {
    private static final String ASSET_NOT_FOUND = "Can't find asset with id %s";
    private static final String MANAGER_NOT_FOUND = "Can't find manager with id %s";
    private static final String STORE_NOT_FOUND = "Manager with id %s doesn't have a store. Please assign him/her first";
    private static final String MINUTE_OF_HANDOVER_NOT_FOUND = "Minute of handover with id %s not found";
    private final MinuteOfHandoverRepository minuteOfHandoverRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final HistoryRepository historyRepository;

    public MinuteOfHandoverServiceImpl(MinuteOfHandoverRepository minuteOfHandoverRepository,
                                       UserRepository userRepository,
                                       AssetRepository assetRepository,
                                       HistoryRepository historyRepository) {
        this.minuteOfHandoverRepository = minuteOfHandoverRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public MinuteOfHandoverDTO add(MinuteOfHandoverDTO dto) {
        Optional<Asset> optionalAsset = assetRepository.findById(dto.getAssetId());
        if (!optionalAsset.isPresent()) {
            throw new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, dto.getAssetId()));
        }
        Optional<User> optionalPreviousManager = userRepository.findById(dto.getPreviousManagerId());
        if (!optionalPreviousManager.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, dto.getPreviousManagerId()));
        }
        Optional<User> optionalCurrentManager = userRepository.findById(dto.getCurrentManagerId());
        if (!optionalCurrentManager.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, dto.getCurrentManagerId()));
        }
        MinuteOfHandover minuteOfHandover = new MinuteOfHandover();
        User currentUser = optionalCurrentManager.get();
        if (currentUser.getStore() == null) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, currentUser.getId()));
        }
        User previousUser = optionalPreviousManager.get();
        if (previousUser.getStore() == null) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, currentUser.getId()));
        }
        return convertEntityToDTO(optionalAsset.get(), currentUser, previousUser, minuteOfHandover, dto);
    }

    @Override
    public MinuteOfHandoverDTO find(long id) {
        Optional<MinuteOfHandover> optionalMinuteOfHandover = minuteOfHandoverRepository.findById(id);
        return optionalMinuteOfHandover.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(MINUTE_OF_HANDOVER_NOT_FOUND, id)));
    }

    @Override
    public Page<MinuteOfHandoverDTO> findAll(Pageable pageable) {
        Page<MinuteOfHandover> minuteOfHandoverPage = minuteOfHandoverRepository.findAll(pageable);
        if (minuteOfHandoverPage.isEmpty()) {
            return Page.empty();
        }
        return minuteOfHandoverPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<MinuteOfHandoverDTO> findAllWithSearch(Pageable pageable, String searchBy, String searchValue) {
        Page<MinuteOfHandover> minuteOfHandoverPage;
        switch (searchBy) {
            case "current_manager_id":
                minuteOfHandoverPage = minuteOfHandoverRepository.findByCurrentUserIdContaining(searchValue, pageable);
                break;
            case "previous_manager_id":
                minuteOfHandoverPage = minuteOfHandoverRepository.findByPreviousUserIdContaining(searchValue, pageable);
                break;
            case "asset_id":
                minuteOfHandoverPage = minuteOfHandoverRepository.findByAssetIdContaining(searchValue, pageable);
                break;
            case "create_by":
                minuteOfHandoverPage = minuteOfHandoverRepository.findByCreateByContaining(searchValue, pageable);
                break;
            case "date":
                minuteOfHandoverPage = minuteOfHandoverRepository.findByDateContaining(searchValue, pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (minuteOfHandoverPage.isEmpty()) {
            return Page.empty();
        }
        return minuteOfHandoverPage.map(this::convertEntityToDTO);
    }

    @Override
    public MinuteOfHandoverDTO update(long id, MinuteOfHandoverDTO dto) {
        Optional<MinuteOfHandover> optionalMinuteOfHandover = minuteOfHandoverRepository.findById(id);
        if (!optionalMinuteOfHandover.isPresent()) {
            throw new ResourceNotFoundException(String.format(MINUTE_OF_HANDOVER_NOT_FOUND, id));
        }
        Optional<Asset> optionalAsset = assetRepository.findById(dto.getAssetId());
        if (!optionalAsset.isPresent()) {
            throw new ResourceNotFoundException(String.format(ASSET_NOT_FOUND, dto.getAssetId()));
        }
        Optional<User> optionalPreviousManager = userRepository.findById(dto.getPreviousManagerId());
        if (!optionalPreviousManager.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, dto.getPreviousManagerId()));
        }
        Optional<User> optionalCurrentManager = userRepository.findById(dto.getCurrentManagerId());
        if (!optionalCurrentManager.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, dto.getCurrentManagerId()));
        }
        MinuteOfHandover minuteOfHandover = optionalMinuteOfHandover.get();
        User currentUser = optionalCurrentManager.get();
        if (currentUser.getStore() == null) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, currentUser.getId()));
        }
        User previousUser = optionalPreviousManager.get();
        if (previousUser.getStore() == null) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, previousUser.getId()));
        }
        return convertEntityToDTO(optionalAsset.get(), currentUser, previousUser, minuteOfHandover, dto);
    }

    @Override
    public void delete(long id) {
        Optional<MinuteOfHandover> optionalMinuteOfHandover = minuteOfHandoverRepository.findById(id);
        if (optionalMinuteOfHandover.isPresent()) {
            minuteOfHandoverRepository.delete(optionalMinuteOfHandover.get());
        } else {
            throw new ResourceNotFoundException(String.format(MINUTE_OF_HANDOVER_NOT_FOUND, id));
        }
    }

    private MinuteOfHandoverDTO convertEntityToDTO(MinuteOfHandover minuteOfHandover) {
        MinuteOfHandoverDTO minuteOfHandoverDTO = new MinuteOfHandoverDTO();
        minuteOfHandoverDTO.setAssetId(minuteOfHandover.getAsset().getId());
        minuteOfHandoverDTO.setCurrentManagerId(minuteOfHandover.getCurrentUser().getId());
        minuteOfHandoverDTO.setCurrentStoreId(minuteOfHandover.getCurrentUser().getStore().getId());
        minuteOfHandoverDTO.setPreviousManagerId(minuteOfHandover.getPreviousUser().getId());
        minuteOfHandoverDTO.setPreviousStoreId(minuteOfHandover.getPreviousUser().getStore().getId());
        minuteOfHandoverDTO.setDate(minuteOfHandover.getDate());
        minuteOfHandoverDTO.setCreateBy(minuteOfHandover.getCreateBy());
        return minuteOfHandoverDTO;
    }

    private MinuteOfHandoverDTO convertEntityToDTO(Asset asset,
                                                   User currentUser,
                                                   User previousUser,
                                                   MinuteOfHandover minuteOfHandover,
                                                   MinuteOfHandoverDTO minuteOfHandoverDTO) {
        asset.setStore(currentUser.getStore());
        asset.setUser(currentUser);
        assetRepository.save(asset);

        History history = new History();
        history.setAsset(asset);
        history.setDepartmentId(asset.getDepartment().getId());
        history.setManagerId(asset.getUser().getId());
        history.setStatusId(asset.getStatus().getId());
        history.setStoreId(asset.getStore().getId());
        history.setExpiryWarrantyDate(asset.getExpiryWarrantyDate());
        history.setNextWarrantyDate(asset.getNextWarrantyDate());
        historyRepository.save(history);

        minuteOfHandover.setPreviousUser(previousUser);
        minuteOfHandover.setCurrentUser(currentUser);
        minuteOfHandover.setAsset(asset);
        minuteOfHandoverRepository.save(minuteOfHandover);

        minuteOfHandoverDTO.setPreviousStoreId(previousUser.getStore().getId());
        minuteOfHandoverDTO.setCurrentManagerId(previousUser.getStore().getId());
        minuteOfHandoverDTO.setDate(minuteOfHandover.getDate());
        minuteOfHandoverDTO.setCreateBy(minuteOfHandover.getCreateBy());
        return minuteOfHandoverDTO;
    }
}
