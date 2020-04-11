package com.swd.controller;

import com.swd.model.dto.DepartmentDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.DepartmentService;
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
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        return departmentService.add(departmentDTO);
    }

    @GetMapping("/{departmentId}")
    public DepartmentDTO findDepartment(@PathVariable(value = "departmentId") long departmentId,
                                        @RequestParam(required = false) List<String> fields) {
        return departmentService.find(departmentId, fields);
    }

    @GetMapping
    public Page<DepartmentDTO> findDepartments(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size,
                                               @RequestParam(required = false) List<String> fields,
                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                               @RequestParam(required = false) SortOrder sortOrder,
                                               @RequestParam(required = false) String searchBy,
                                               @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findDepartmentsWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findDepartmentsWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<DepartmentDTO> findDepartmentsWithSearch(Pageable pageable,
                                                          List<String> fields,
                                                          String searchBy,
                                                          String searchValue) {
        if (searchBy != null && searchValue != null) {
            return departmentService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return departmentService.findAll(pageable, fields);
    }

    @PutMapping("/{departmentId}")
    public DepartmentDTO updateDepartment(@PathVariable(value = "departmentId") long departmentId,
                                          @Valid @RequestBody DepartmentDTO departmentDTO) {
        return departmentService.update(departmentId, departmentDTO);
    }

    @DeleteMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable(value = "departmentId") long departmentId) {
        departmentService.delete(departmentId);
    }
}
