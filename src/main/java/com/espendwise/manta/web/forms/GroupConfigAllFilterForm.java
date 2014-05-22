package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupConfigAllFilterFormValidator;


@Validation(GroupConfigAllFilterFormValidator.class)
public class GroupConfigAllFilterForm extends GroupConfigFilterForm {
	private boolean showConfiguredOnly = true;
    private boolean showInactive = false;
	
	public GroupConfigAllFilterForm(Long groupId) {
		super(groupId);
	}
	
	public void setShowConfiguredOnly(boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}

	public boolean getShowConfiguredOnly() {
		return showConfiguredOnly;
	}
	
	public void setShowInactive(boolean showInactive) {
		this.showInactive = showInactive;
	}

	public boolean isShowInactive() {
		return showInactive;
	}
}
