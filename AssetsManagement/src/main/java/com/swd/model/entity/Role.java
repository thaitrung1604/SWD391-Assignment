package com.swd.model.entity;

import javax.persistence.Entity;

@Entity
public class Role extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
