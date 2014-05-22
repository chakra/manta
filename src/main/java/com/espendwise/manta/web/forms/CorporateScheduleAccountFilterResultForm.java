package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class CorporateScheduleAccountFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<BusEntityData>> {

    private SelectableObjects<BusEntityData> accounts;
    private Boolean removeSitesWithAccounts;

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
        this.accounts = null;
        this.removeSitesWithAccounts = false;
    }

    public Boolean getRemoveSitesWithAccounts() {
        return removeSitesWithAccounts;
    }

    public void setRemoveSitesWithAccounts(Boolean removeSitesWithAccounts) {
        this.removeSitesWithAccounts = removeSitesWithAccounts;
    }

}
