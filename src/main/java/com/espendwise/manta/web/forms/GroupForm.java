package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupFormValidator;

@Validation(GroupFormValidator.class)
public class GroupForm extends WebForm implements Initializable {
    //base
    private Long groupId;
    private String groupName;
    private String groupStatus;
    private String groupType;
    private Long assocStoreId = new Long(0);
    private String assocStoreName;
    private Long storeIdCanAssoc;    
    
    private boolean init;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String status) {
        this.groupStatus = status;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
    
    public Long getAssocStoreId() {
        return this.assocStoreId;
    }

    public void setAssocStoreId(Long assocStoreId) {
        this.assocStoreId = assocStoreId;
    }

    public String getAssocStoreName() {
        return this.assocStoreName;
    }

    public void setAssocStoreName(String assocStoreName) {
        this.assocStoreName = assocStoreName;
    }    
    
    public void setStoreIdCanAssoc(Long storeIdCanAssoc) {
		this.storeIdCanAssoc = storeIdCanAssoc;
	}

	public Long getStoreIdCanAssoc() {
		return storeIdCanAssoc;
	}

	@Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

    public boolean isNew() {
        return isInitialized() && (groupId == null || groupId == 0);
    }	
}
