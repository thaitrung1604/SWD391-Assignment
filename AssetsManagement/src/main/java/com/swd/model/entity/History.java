package com.swd.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class History extends AuditEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Asset asset;
    private Long statusId;
    private Long departmentId;
    private Long storeId;
    private Long userId;
    private Date nextWarrantyDate;
    private Date expiryWarrantyDate;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getNextWarrantyDate() {
        return nextWarrantyDate;
    }

    public void setNextWarrantyDate(Date nextWarrantyDate) {
        this.nextWarrantyDate = nextWarrantyDate;
    }

    public Date getExpiryWarrantyDate() {
        return expiryWarrantyDate;
    }

    public void setExpiryWarrantyDate(Date expiryWarrantyDate) {
        this.expiryWarrantyDate = expiryWarrantyDate;
    }
}
