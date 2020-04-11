package com.swd.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StatusDTO extends BaseDTO {
    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
