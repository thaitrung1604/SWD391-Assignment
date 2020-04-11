package com.swd.controller;

import com.swd.model.dto.HistoryDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/histories")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{historyId}")
    public HistoryDTO findHistory(@PathVariable(value = "historyId") long historyId) {
        return historyService.find(historyId);
    }

    @GetMapping
    public Page<HistoryDTO> findHistories(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size,
                                          @RequestParam(required = false, defaultValue = "id") String sortBy,
                                          @RequestParam(required = false) SortOrder sortOrder,
                                          @RequestParam(required = false) String searchBy,
                                          @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findHistoriesWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), searchBy, searchValue);
        }
        return findHistoriesWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), searchBy, searchValue);
    }

    private Page<HistoryDTO> findHistoriesWithSearch(Pageable pageable,
                                                     String searchBy,
                                                     String searchValue) {
        if (searchBy != null && searchValue != null) {
            return historyService.findAllWithSearch(pageable, searchBy, searchValue);
        }
        return historyService.findAll(pageable);
    }
}
