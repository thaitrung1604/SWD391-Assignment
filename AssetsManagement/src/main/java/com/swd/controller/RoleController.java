package com.swd.controller;

import com.swd.model.dto.RoleDTO;
import com.swd.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/{roleId}")
    public RoleDTO findRole(@PathVariable(value = "roleId") long roleId) {
        return roleService.find(roleId);
    }

    @GetMapping
    public List<RoleDTO> findRoles() {
        return roleService.findAll();
    }
}
