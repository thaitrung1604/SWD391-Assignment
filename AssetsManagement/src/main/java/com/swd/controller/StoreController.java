package com.swd.controller;

import com.swd.model.dto.StoreDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/admin/stores")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreDTO addStore(@Valid @RequestBody StoreDTO storeDTO) {
        return storeService.add(storeDTO);
    }

    @GetMapping("/stores/{storeId}")
    public StoreDTO findStore(@PathVariable(value = "storeId") long storeId,
                              @RequestParam(required = false) List<String> fields) {
        return storeService.find(storeId, fields);
    }

    @GetMapping("/stores")
    public Page<StoreDTO> findStores(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "10") int size,
                                     @RequestParam(required = false) List<String> fields,
                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                     @RequestParam(required = false) SortOrder sortOrder,
                                     @RequestParam(required = false) String searchBy,
                                     @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findStoresWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findStoresWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<StoreDTO> findStoresWithSearch(Pageable pageable,
                                                List<String> fields,
                                                String searchBy,
                                                String searchValue) {
        if (searchBy != null && searchValue != null) {
            return storeService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return storeService.findAll(pageable, fields);
    }

    @PutMapping("/admin/stores/{storeId}")
    public StoreDTO updateStore(@PathVariable(value = "storeId") long storeId,
                                @Valid @RequestBody StoreDTO storeDTO) {
        return storeService.update(storeId, storeDTO);
    }

    @DeleteMapping("/admin/stores/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable(value = "storeId") long storeId) {
        storeService.delete(storeId);
    }
}
