package com.swd.controller;

import com.swd.model.dto.StatusDTO;
import com.swd.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statuses")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/{statusId}")
    public StatusDTO findStatus(@PathVariable(value = "statusId") long statusId) {
        return statusService.find(statusId);
    }

    @GetMapping
    public List<StatusDTO> findStatuses() {
        return statusService.findAll();
    }
}
