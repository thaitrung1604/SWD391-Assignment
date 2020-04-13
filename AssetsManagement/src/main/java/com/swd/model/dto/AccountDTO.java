package com.swd.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDTO extends BaseDTO {
    @NotNull
    @Size(min = 2, max = 20)
    private String username;
    @NotNull
    @Size(min = 2, max = 20)
    private String password;
    private boolean isEnabled;
    @Valid
    private UserDTO user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
