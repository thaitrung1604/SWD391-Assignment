package com.swd.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Asset extends BaseEntity {
    private String name;
    private String description;
    private double price;
    private Date purchaseDate;
    private Date expiryWarrantyDate;
    private Date nextWarrantyDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpiryWarrantyDate() {
        return expiryWarrantyDate;
    }

    public void setExpiryWarrantyDate(Date expiryWarrantyDate) {
        this.expiryWarrantyDate = expiryWarrantyDate;
    }

    public Date getNextWarrantyDate() {
        return nextWarrantyDate;
    }

    public void setNextWarrantyDate(Date nextWarrantyDate) {
        this.nextWarrantyDate = nextWarrantyDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
