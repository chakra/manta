package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class StoreMsgAccountconfigForm extends AbstractFilterResult<SelectableObjects.SelectableObject<BusEntityData>> {

    private SelectableObjects<BusEntityData> accounts;
    private Long storeMessageId;
    private Boolean messageManagedByCustomer;

    public StoreMsgAccountconfigForm() {
        super();
    }

    public StoreMsgAccountconfigForm(Long storeMessageId) {
        super();
        this.storeMessageId = storeMessageId;
    }

    public Long getStoreMessageId() {
        return storeMessageId;
    }

    public void setStoreMessageId(Long storeMessageId) {
        this.storeMessageId = storeMessageId;
    }

    public Boolean getMessageManagedByCustomer() {
        return messageManagedByCustomer;
    }

    public void setMessageManagedByCustomer(Boolean messageManagedByCustomer) {
        this.messageManagedByCustomer = messageManagedByCustomer;
    }

    public SelectableObjects<BusEntityData> getAccounts() {
        return accounts;
    }

    public void setAccounts(SelectableObjects<BusEntityData> accounts) {
        this.accounts = accounts;
    }

    @Override
    public List<SelectableObjects.SelectableObject<BusEntityData>> getResult() {
        return accounts != null ? accounts.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        accounts = null;
        messageManagedByCustomer = null;
    }
}
