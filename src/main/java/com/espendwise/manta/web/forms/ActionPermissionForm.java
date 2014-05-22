package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class ActionPermissionForm implements Serializable {

    boolean canEdit;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
