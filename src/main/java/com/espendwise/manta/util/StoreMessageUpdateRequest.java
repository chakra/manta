package com.espendwise.manta.util;


import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.entity.StoreMessageEntity;

import java.io.Serializable;

public class StoreMessageUpdateRequest implements Serializable {

    private Long storeId;
    private boolean applyValidationRuie;
    private boolean clearViewHistory;
    private StoreMessageEntity storeMessage;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public boolean isApplyValidationRuie() {
        return applyValidationRuie;
    }

    public void setApplyValidationRuie(boolean applyValidationRuie) {
        this.applyValidationRuie = applyValidationRuie;
    }

    public StoreMessageEntity getStoreMessage() {
        return storeMessage;
    }

    public void setStoreMessage(StoreMessageEntity storeMessage) {
        this.storeMessage = storeMessage;
    }

    public boolean isClearViewHistory() {
        return clearViewHistory;
    }

    public void setClearViewHistory(boolean clearViewHistory) {
        this.clearViewHistory = clearViewHistory;
    }
}
