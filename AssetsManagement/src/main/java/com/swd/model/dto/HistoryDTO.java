package com.swd.model.dto;

import java.util.Date;

public class HistoryDTO {
    private Long assetId;
    private Long statusId;
    private Long departmentId;
    private Long storeId;
    private Long managerId;
    private Date nextWarrantyDate;
    private Date expiryWarrantyDate;
    private Date date;
    private Long createBy;

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
