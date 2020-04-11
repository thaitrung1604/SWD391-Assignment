package com.swd.controller;

import com.swd.exception.NotSupportedException;
import com.swd.model.dto.AssetDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.AssetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetDTO addAsset(@Valid @RequestBody AssetDTO assetDTO) {
        return assetService.add(assetDTO);
    }

    @GetMapping("/{assetId}")
    public AssetDTO findAsset(@PathVariable(value = "assetId") long assetId,
                              @RequestParam(required = false) List<String> fields) {
        return assetService.find(assetId, fields);
    }

    @GetMapping
    public Page<AssetDTO> findAssets(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "10") int size,
                                     @RequestParam(required = false) List<String> fields,
                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                     @RequestParam(required = false) SortOrder sortOrder,
                                     @RequestParam(required = false) String searchBy,
                                     @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findAssetsWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findAssetsWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<AssetDTO> findAssetsWithSearch(Pageable pageable,
                                                List<String> fields,
                                                String searchBy,
                                                String searchValue) {
        if (searchBy != null && searchValue != null) {
            return assetService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return assetService.findAll(pageable, fields);
    }

    @PutMapping("/{assetId}")
    public AssetDTO updateAsset(@PathVariable(value = "assetId") long assetId,
                                @Valid @RequestBody AssetDTO assetDTO) {
        return assetService.update(assetId, assetDTO);
    }

    @PatchMapping("/{assetId}")
    public AssetDTO updateAssetStatusOrDepartment(@PathVariable(value = "assetId") long assetId,
                                                  @RequestParam(defaultValue = "status") String update,
                                                  @RequestParam long id) {
        if (update.equals("status")) {
            return assetService.updateStatus(assetId, id);
        } else if (update.equals("department")) {
            return assetService.updateDepartment(assetId, id);
        }
        throw new NotSupportedException("System doesn't support update " + update);
    }

    @DeleteMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(@PathVariable(value = "assetId") long assetId) {
        assetService.delete(assetId);
    }
}
