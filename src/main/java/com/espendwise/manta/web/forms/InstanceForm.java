package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.InstanceView;
import com.espendwise.manta.web.util.SortHistory;

import java.util.List;

public class InstanceForm extends AbstractSimpleFilterForm implements IModelAttribute, FilterResult<InstanceView> {

    private List<InstanceView> userStores;
    private SortHistory sortHistory;
    private boolean isListView;
    private boolean isSearchView;

    public InstanceForm() {
        setFilterType(com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH);
    }

    public List<InstanceView> getUserStores() {
        return userStores;
    }

    public void setUserStores(List<InstanceView> userStores) {
        this.userStores = userStores;
    }

    public List<InstanceView> getResult() {
        return getUserStores();
    }

    public void setIsListView(boolean listView) {
        isListView = listView;
    }

    public void setIsSearchView(boolean searchView) {
        isSearchView = searchView;
    }

    public boolean getIsListView() {
        return isListView;
    }

    public boolean getIsSearchView() {
        return isSearchView;
    }

    @Override
    public void setSortHistory(SortHistory history) {
       this.sortHistory = history;
    }

    @Override
    public SortHistory getSortHistory() {
        return this.sortHistory;
    }

}
