package com.swd.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class MinuteOfHandoverDTO extends BaseDTO {
    @NotNull
    private Long assetId;
    @NotNull
    private Long previousManagerId;
    @NotNull
    private Long currentManagerId;
    private Long previousStoreId;
    private Long currentStoreId;
    private Date date;
    private Long createBy;

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getPreviousManagerId() {
        return previousManagerId;
    }

    public void setPreviousManagerId(Long previousManagerId) {
        this.previousManagerId = previousManagerId;
    }

    public Long getCurrentManagerId() {
        return currentManagerId;
    }

    public void setCurrentManagerId(Long currentManagerId) {
        this.currentManagerId = currentManagerId;
    }

    public Long getPreviousStoreId() {
        return previousStoreId;
    }

    public void setPreviousStoreId(Long previousStoreId) {
        this.previousStoreId = previousStoreId;
    }

    public Long getCurrentStoreId() {
        return currentStoreId;
    }

    public void setCurrentStoreId(Long currentStoreId) {
        this.currentStoreId = currentStoreId;
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
