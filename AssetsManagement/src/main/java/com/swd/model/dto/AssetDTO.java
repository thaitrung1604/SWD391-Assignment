package com.swd.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class AssetDTO extends BaseDTO {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private double price;
    @NotNull
    private Date purchaseDate;
    @NotNull
    private Date expiryWarrantyDate;
    @NotNull
    private Date nextWarrantyDate;
    private TypeDTO type;
    private StatusDTO status;
    private DepartmentDTO department;
    private SupplierDTO supplier;
    private StoreDTO store;
    private ManagerDTO manager;

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

    public TypeDTO getType() {
        return type;
    }

    public void setType(TypeDTO type) {
        this.type = type;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }
}
