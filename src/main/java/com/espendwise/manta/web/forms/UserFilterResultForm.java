package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.UserListView;
import java.util.List;

public class UserFilterResultForm  extends AbstractFilterResult<UserListView> {

    private List<UserListView> users;

    public List<UserListView> getUsers() {
        return users;
    }

    public void setUsers(List<UserListView> users) {
        this.users = users;
    }

    @Override
    public List<UserListView> getResult() {
        return users;
    }

    @Override
    public void reset() {
        super.reset();
        this.users = null;
    }


}
