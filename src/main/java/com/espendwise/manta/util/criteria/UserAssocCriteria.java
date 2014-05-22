package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class UserAssocCriteria implements Serializable {

    private List<Long> entityIds;
    private List<String> userTypes;
    private boolean activeUserOnly = false;

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }

    public List<String> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }

	public void setActiveUserOnly(boolean activeUserOnly) {
		this.activeUserOnly = activeUserOnly;
	}

	public boolean isActiveUserOnly() {
		return activeUserOnly;
	}
}
