package com.swd.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Department extends BaseEntity {
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;

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
}
