package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;

import java.util.List;

public class AccountFilterResultForm  extends AbstractFilterResult<AccountListView> {

    private List<AccountListView> accounts;

    public List<AccountListView> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountListView> accounts) {
        this.accounts = accounts;
    }

    @Override
    public List<AccountListView> getResult() {
        return accounts;
    }

    @Override
    public void reset() {
        super.reset();
        this.accounts = null;
    }


}
