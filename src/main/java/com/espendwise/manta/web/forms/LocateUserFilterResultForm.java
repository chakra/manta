package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.UserListView;

import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateUserFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<UserListView>> {

    private SelectableObjects<UserListView> selectedUsers;
    private boolean multiSelected;

    public SelectableObjects<UserListView> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(SelectableObjects<UserListView> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    @Override
    public List<SelectableObjects.SelectableObject<UserListView>> getResult() {
        return selectedUsers != null ? selectedUsers.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedUsers = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
