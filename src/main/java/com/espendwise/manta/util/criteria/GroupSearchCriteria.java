package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class GroupSearchCriteria implements Serializable {

    private String groupName;
    private String groupStatus;
    private Long groupId;
    private List<Long> groupIds;
    private Long userId;
    private List<Long> storeIds;
    private String  groupNameMatchType;
    private String groupType;
    private boolean activeOnly;
    private List<String> groupNameIn;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}	
	
    public List<Long> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
    }

    public String getGroupNameMatchType() {
        return groupNameMatchType;
    }

    public void setGroupNameMatchType(String  groupNameMatchType) {
        this.groupNameMatchType = groupNameMatchType;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public void setGroupNameIn(List<String> groupNameIn) {
        this.groupNameIn = groupNameIn;
    }

    public List<String> getGroupNameIn() {
        return groupNameIn;
    }

    @Override
    public String toString() {
        return "GroupSearchCriteria{" +
                "groupName='" + groupName + '\'' +
                ", groupStatus='" + groupStatus + '\'' +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", storeIds=" + storeIds +
                ", groupNameMatchType='" + groupNameMatchType + '\'' +
                ", groupType='" + groupType + '\'' +
                ", activeOnly='" + activeOnly + '\'' +
                ", groupNameIn=" + groupNameIn +
                '}';
    }
}
