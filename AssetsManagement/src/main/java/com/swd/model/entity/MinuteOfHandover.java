package com.swd.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class MinuteOfHandover extends AuditEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Asset asset;
    @ManyToOne(fetch = FetchType.LAZY)
    private User previousUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private User currentUser;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public User getPreviousUser() {
        return previousUser;
    }

    public void setPreviousUser(User previousUser) {
        this.previousUser = previousUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
