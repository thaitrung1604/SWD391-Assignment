package com.swd.controller;

import com.swd.model.dto.UserDTO;
import com.swd.model.myenum.SortOrder;
import com.swd.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @GetMapping("/users/{userId}")
    public UserDTO findUser(@PathVariable(value = "userId") long userId,
                            @RequestParam(required = false) List<String> fields) {
        return userService.find(userId, fields);
    }

    @GetMapping("/users")
    public Page<UserDTO> findUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "10") int size,
                                   @RequestParam(required = false) List<String> fields,
                                   @RequestParam(required = false, defaultValue = "id") String sortBy,
                                   @RequestParam(required = false) SortOrder sortOrder,
                                   @RequestParam(required = false) String searchBy,
                                   @RequestParam(required = false) String searchValue) {
        if (sortOrder == SortOrder.DESC) {
            return findUsersWithSearch(
                    PageRequest.of(page, size, Sort.by(sortBy).descending()), fields, searchBy, searchValue);
        }
        return findUsersWithSearch(
                PageRequest.of(page, size, Sort.by(sortBy).ascending()), fields, searchBy, searchValue);
    }

    private Page<UserDTO> findUsersWithSearch(Pageable pageable,
                                              List<String> fields,
                                              String searchBy,
                                              String searchValue) {
        if (searchBy != null && searchValue != null) {
            return userService.findAllWithSearch(pageable, fields, searchBy, searchValue);
        }
        return userService.findAll(pageable, fields);
    }

    @PutMapping("/admin/users/{userId}")
    public UserDTO updateUser(@PathVariable(value = "userId") long userId,
                              @Valid @RequestBody UserDTO userDTO) {
        return userService.update(userId, userDTO);
    }

    @PatchMapping("/admin/users/{userId}")
    public void updateUserStore(@PathVariable(value = "userId") long userId,
                                @RequestParam long storeId) {
        userService.updateUserStore(userId, storeId);
    }
}
