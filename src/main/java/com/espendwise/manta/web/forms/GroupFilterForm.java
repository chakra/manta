package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupFilterFormValidator;


@Validation(GroupFilterFormValidator.class)
public class GroupFilterForm extends WebForm implements Resetable, Initializable {

    private String groupId;
    private String groupName;
    private String groupType;
    private String groupNameFilterType;
    private Boolean showInactive;
    private Boolean showAll;
    private String assocStoreName;

    private boolean init;


    public GroupFilterForm() {
        super();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

    public String getGroupType() {
		return groupType;
	}

	public void setGroupNameFilterType(String groupNameFilterType) {
		this.groupNameFilterType = groupNameFilterType;
	}

	public String getGroupNameFilterType() {
		return groupNameFilterType;
	}

	public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public Boolean getShowAll() {
        return showAll;
    }

    public void setShowAll(Boolean showAll) {
        this.showAll = showAll;
    }

    public void setAssocStoreName(String assocStoreName) {
		this.assocStoreName = assocStoreName;
	}

	public String getAssocStoreName() {
		return assocStoreName;
	}

	@Override
    public void initialize() {
        reset();
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.groupId = null;
        this.groupName = null;
        this.setGroupType(null);
        this.setGroupNameFilterType(Constants.FILTER_TYPE.CONTAINS);
        this.showInactive = false;
        this.showAll = false;
        this.assocStoreName = null;
    }

}
