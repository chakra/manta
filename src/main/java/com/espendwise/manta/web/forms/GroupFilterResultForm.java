package com.espendwise.manta.web.forms;

import java.util.List;

import com.espendwise.manta.model.view.GroupListView;


public class GroupFilterResultForm  extends AbstractFilterResult<GroupListView> {

    private List<GroupListView> groups;

    public GroupFilterResultForm() {
    }

    public List<GroupListView> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupListView> groups) {
        this.groups = groups;
    }

    @Override
    public List<GroupListView> getResult() {
        return groups;
    }

    @Override
    public void reset() {
        super.reset();
        this.groups = null;
    }


}
