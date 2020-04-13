package com.swd.controller;

import com.swd.model.dto.MinuteOfHandoverDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.MinuteOfHandoverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/minuteofhandovers")
public class MinuteOfHandoverController {
    private final MinuteOfHandoverService minuteOfHandoverService;

    public MinuteOfHandoverController(MinuteOfHandoverService minuteOfHandoverService) {
        this.minuteOfHandoverService = minuteOfHandoverService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MinuteOfHandoverDTO addMinuteOfHandover(@Valid @RequestBody MinuteOfHandoverDTO minuteOfHandoverDTO) {
        return minuteOfHandoverService.add(minuteOfHandoverDTO);
    }

    @GetMapping("/{minuteOfHandoverId}")
    public MinuteOfHandoverDTO findMinuteOfHandover(@PathVariable(value = "minuteOfHandoverId") long minuteOfHandoverId) {
        return minuteOfHandoverService.find(minuteOfHandoverId);
    }

    @GetMapping
    public Page<MinuteOfHandoverDTO> findMinuteOfHandovers(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false) SortOrder sortOrder,
                                                           @RequestParam(required = false) String searchBy,
                                                           @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findMinuteOfHandoversWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), searchBy, searchValue);
        }
        return findMinuteOfHandoversWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), searchBy, searchValue);
    }

    private Page<MinuteOfHandoverDTO> findMinuteOfHandoversWithSearch(Pageable pageable,
                                                                      String searchBy,
                                                                      String searchValue) {
        if (searchBy != null && searchValue != null) {
            return minuteOfHandoverService.findAllWithSearch(pageable, searchBy, searchValue);
        }
        return minuteOfHandoverService.findAll(pageable);
    }
}
