package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;

import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateAccountFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<AccountListView>> {

    private SelectableObjects<AccountListView> selectedAccounts;
    private boolean multiSelected;

    public SelectableObjects<AccountListView> getSelectedAccounts() {
        return selectedAccounts;
    }

    public void setSelectedAccounts(SelectableObjects<AccountListView> selectedAccounts) {
        this.selectedAccounts = selectedAccounts;
    }

    @Override
    public List<SelectableObjects.SelectableObject<AccountListView>> getResult() {
        return selectedAccounts != null ? selectedAccounts.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedAccounts = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
