package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.model.view.StoreMessageListView;

public class StoreMessageFilterResultForm  extends AbstractFilterResult<StoreMessageListView> {

    private List<StoreMessageListView> storeMessages;

    public List<StoreMessageListView> getStoreMessages() {
        return storeMessages;
    }

    public void setStoreMessages(List<StoreMessageListView> storeMessages) {
        this.storeMessages = storeMessages;
    }

    @Override
    public List<StoreMessageListView> getResult() {
        return storeMessages;
    }

    @Override
    public void reset() {
        super.reset();
        storeMessages = null;
    }
}
