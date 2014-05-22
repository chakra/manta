package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.UserGroupInfLineView;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class UserGroupFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<Pair<Long, String>>> {

    private FilterResultWebForm<UserGroupInfLineView> groupsInformation;
    private SelectableObjects<Pair<Long, String>> groups;
    private Long userId;
    private String userType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public FilterResultWebForm<UserGroupInfLineView> getGroupsInformation() {
        return groupsInformation;
    }

    public void setGroupsInformation(FilterResultWebForm<UserGroupInfLineView> groupsInformation) {
        this.groupsInformation = groupsInformation;
    }

    public SelectableObjects<Pair<Long, String>> getGroups() {
        return groups;
    }

    public void setGroups(SelectableObjects<Pair<Long,String>> groups) {
        this.groups = groups;
    }

    @Override
    public List<SelectableObjects.SelectableObject<Pair<Long, String>>> getResult() {
        return groups == null ? null : groups.getSelectableObjects();
    }

    @Override
    public void reset() {
        super.reset();
        this.groups = null;
        this.groupsInformation = null;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}
