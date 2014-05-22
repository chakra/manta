package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class SiteUserFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<UserListView>> {

    private SelectableObjects<UserListView> users;
    private String primaryUser;

    public SelectableObjects<UserListView> getUsers() {
        return users;
    }

    public void setUsers(SelectableObjects<UserListView> users) {
        this.users = users;
    }

    @Override
    public List<SelectableObjects.SelectableObject<UserListView>> getResult() {
        return users != null ? users.getSelectableObjects() : null;
    }

    public String getPrimaryUser() {
        return primaryUser;
    }

    public void setPrimaryUser(String primaryUser) {
        this.primaryUser = primaryUser;
    }

    @Override
    public void reset() {
        super.reset();
        this.users = null;
    }

}
