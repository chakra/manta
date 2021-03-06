package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
/**
 * GroupView generated by hbm2java
*/
public class GroupView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String GROUP_ID = "groupId";
    public static final String GROUP_NAME = "groupName";

    private Long groupId;
    private String groupName;

    public GroupView() {
    }
	
    public GroupView(Long groupId) {
        this.setGroupId(groupId);
    }

    public GroupView(Long groupId, String groupName) {
        this.setGroupId(groupId);
        this.setGroupName(groupName);
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
        setDirty(true);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        setDirty(true);
    }




}


