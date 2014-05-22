package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.UserGroupFilterFormValidator;

@Validation(UserGroupFilterFormValidator.class)
public class UserGroupFilterForm extends WebForm implements Resetable, Initializable {

    private String groupId;
    private Long userId;
    private String groupNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private String groupName;
    private boolean onlyAssociatedGroups;

    private boolean init;


    public UserGroupFilterForm(Long userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupNameFilterType() {
        return groupNameFilterType;
    }

    public void setGroupNameFilterType(String groupNameFilterType) {
        this.groupNameFilterType = groupNameFilterType;
    }

    public boolean isOnlyAssociatedGroups() {
        return onlyAssociatedGroups;
    }

    public boolean getOnlyAssociatedGroups() {
        return onlyAssociatedGroups;
    }

    public void setOnlyAssociatedGroups(boolean onlyAssociatedGroups) {
        this.onlyAssociatedGroups = onlyAssociatedGroups;
    }

    @Override
    public void initialize() {
        reset();
        this.groupNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.onlyAssociatedGroups = false;
        this.groupId = null;
        this.groupName = null;
        this.groupNameFilterType = Constants.FILTER_TYPE.START_WITH;
    }


    public Long getUserId() {
        return userId;
    }

    public Long setUserId() {
        return userId;
    }

}
