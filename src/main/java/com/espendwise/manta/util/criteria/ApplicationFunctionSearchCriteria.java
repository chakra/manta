package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class ApplicationFunctionSearchCriteria implements Serializable {

    private Long groupId;
    private Long userId;
    private String functionName;
    private String functionType;
    private String  functionNameMatchType;
    private boolean showConfiguredOnly;


    @Override
    public String toString() {
        return "FunctionSearchCriteria{" +
                "groupId='" + groupId + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", functionName=" + functionName +
                ", functionType=" + functionType +
                ", functionNameMatchType='" + functionNameMatchType + '\'' +
                ", showConfiguredOnly='" + showConfiguredOnly +
                '}';
    }

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionNameMatchType(String functionNameMatchType) {
		this.functionNameMatchType = functionNameMatchType;
	}

	public String getFunctionNameMatchType() {
		return functionNameMatchType;
	}

	public void setShowConfiguredOnly(boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}

	public boolean isShowConfiguredOnly() {
		return showConfiguredOnly;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFunctionType() {
		return functionType;
	}
}
