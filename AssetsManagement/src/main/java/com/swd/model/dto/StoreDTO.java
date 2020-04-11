package com.swd.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class StoreDTO extends BaseDTO {
    @NotNull
    @Size(min = 2, max = 20)
    private String name;
    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = " must have 10 digits")
    private String phone;
    @NotNull
    @Size(min = 2, max = 50)
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
