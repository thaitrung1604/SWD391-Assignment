package com.swd.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO extends BaseDTO {
    @NotNull
    @Size(min = 2, max = 20)
    private String name;
    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = " must have 10 digits")
    private String phone;
    @NotNull
    @Email(message = "Please check your email format")
    private String email;
    private StoreDTO store;
    @NotNull
    @Valid
    private AccountDTO account;
    @NotNull
    private RoleDTO role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}
