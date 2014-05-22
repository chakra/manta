package com.espendwise.manta.util.criteria;

import java.io.Serializable;

import com.espendwise.manta.util.RefCodeNames;

public class GroupConfigSearchCriteria implements Serializable {

    private Long groupId;
    private String groupType;
    private Long userId;
    private Long storeId;
    private String searchId;
    private String searchName;
    private String searchNameMatchType;
    private boolean showConfiguredOnly;
    private boolean showInactive;
    private Integer limit;

    public GroupConfigSearchCriteria(Long groupId, String groupType, Long userId, Integer limit){
    	this.groupId = groupId;
    	this.groupType = groupType;
    	this.userId = userId;
    	this.limit = limit;
    }
    
    @Override
    public String toString() {
        return "FunctionSearchCriteria{" +
                "groupId='" + groupId + '\'' +
                ", groupType='" + groupType + '\'' +
                ", userId='" + userId + '\'' +
                ", storeId=" + storeId + '\'' +
                ", searchId=" + searchId + '\'' +
                ", searchName=" + searchName + '\'' +
                ", searchNameMatchType=" + searchNameMatchType + '\'' +
                ", showConfiguredOnly='" + showConfiguredOnly +
                ", showInactive='" + showInactive +
                ", limit='" + limit +
                '}';
    }

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupType() {
		return groupType;
	}

	public String getGroupAssocCd() {
    	return getGroupType().equals(RefCodeNames.GROUP_TYPE_CD.USER) ? RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP 
        		: RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStoreId() {
		return storeId;
	}
	
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public String getSearchName() {
		return searchName;
	}
	
	public void setSearchNameMatchType(String searchNameMatchType) {
		this.searchNameMatchType = searchNameMatchType;
	}

	public String getSearchNameMatchType() {
		return searchNameMatchType;
	}

	public void setShowConfiguredOnly(boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}

	public boolean isShowConfiguredOnly() {
		return showConfiguredOnly;
	}	
	
	public void setShowInactive(boolean showInactive) {
		this.showInactive = showInactive;
	}

	public boolean isShowInactive() {
		return showInactive;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getLimit() {
		return limit;
	}

	
}
