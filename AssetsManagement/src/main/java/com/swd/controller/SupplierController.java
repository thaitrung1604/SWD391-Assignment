package com.swd.controller;

import com.swd.model.dto.SupplierDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.SupplierService;
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
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierDTO addSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        return supplierService.add(supplierDTO);
    }

    @GetMapping("/{supplierId}")
    public SupplierDTO findSupplier(@PathVariable(value = "supplierId") long supplierId,
                                    @RequestParam(required = false) List<String> fields) {
        return supplierService.find(supplierId, fields);
    }

    @GetMapping
    public Page<SupplierDTO> findSuppliers(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size,
                                           @RequestParam(required = false) List<String> fields,
                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                           @RequestParam(required = false) SortOrder sortOrder,
                                           @RequestParam(required = false) String searchBy,
                                           @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findSuppliersWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findSuppliersWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<SupplierDTO> findSuppliersWithSearch(Pageable pageable,
                                                      List<String> fields,
                                                      String searchBy,
                                                      String searchValue) {
        if (searchBy != null && searchValue != null) {
            return supplierService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return supplierService.findAll(pageable, fields);
    }

    @PutMapping("/{supplierId}")
    public SupplierDTO updateSupplier(@PathVariable(value = "supplierId") long supplierId,
                                      @Valid @RequestBody SupplierDTO supplierDTO) {
        return supplierService.update(supplierId, supplierDTO);
    }

    @DeleteMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplier(@PathVariable(value = "supplierId") long supplierId) {
        supplierService.delete(supplierId);
    }
}
