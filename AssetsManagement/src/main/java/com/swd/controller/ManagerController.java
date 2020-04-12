package com.swd.controller;

import com.swd.model.dto.ManagerDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.ManagerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/managers")
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/{managerId}")
    public ManagerDTO findManager(@PathVariable(value = "managerId") long managerId,
                                  @RequestParam(required = false) List<String> fields) {
        return managerService.find(managerId, fields);
    }

    @GetMapping
    public Page<ManagerDTO> findManagers(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         @RequestParam(required = false) List<String> fields,
                                         @RequestParam(required = false, defaultValue = "id") String sortBy,
                                         @RequestParam(required = false) SortOrder sortOrder,
                                         @RequestParam(required = false) String searchBy,
                                         @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findManagersWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findManagersWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<ManagerDTO> findManagersWithSearch(Pageable pageable,
                                                    List<String> fields,
                                                    String searchBy,
                                                    String searchValue) {
        if (searchBy != null && searchValue != null) {
            return managerService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return managerService.findAll(pageable, fields);
    }

    @PutMapping("/{managerId}")
    public ManagerDTO updateManager(@PathVariable(value = "managerId") long managerId,
                                    @Valid @RequestBody ManagerDTO managerDTO) {
        return managerService.update(managerId, managerDTO);
    }

    @PatchMapping("/{managerId}")
    public void updateManagerStore(@PathVariable(value = "managerId") long managerId,
                                   @RequestParam long storeId) {
        managerService.updateManagerStore(managerId, storeId);
    }
}
