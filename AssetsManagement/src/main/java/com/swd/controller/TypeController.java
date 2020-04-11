package com.swd.controller;

import com.swd.model.dto.TypeDTO;
import com.swd.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
public class TypeController {
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }


    @GetMapping("/{typeId}")
    public TypeDTO findType(@PathVariable(value = "typeId") long typeId) {
        return typeService.find(typeId);
    }

    @GetMapping
    public List<TypeDTO> findTypes() {
        return typeService.findAll();
    }
}
