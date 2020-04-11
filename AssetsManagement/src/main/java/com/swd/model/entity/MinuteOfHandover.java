package com.swd.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class MinuteOfHandover extends AuditEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Asset asset;
    @ManyToOne(fetch = FetchType.LAZY)
    private Manager previousManager;
    @ManyToOne(fetch = FetchType.LAZY)
    private Manager currentManager;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Manager getPreviousManager() {
        return previousManager;
    }

    public void setPreviousManager(Manager previousManager) {
        this.previousManager = previousManager;
    }

    public Manager getCurrentManager() {
        return currentManager;
    }

    public void setCurrentManager(Manager currentManager) {
        this.currentManager = currentManager;
    }
}
