package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupConfigFilterFormValidator;


@Validation(GroupConfigFilterFormValidator.class)
public class GroupConfigFilterForm extends WebForm implements Resetable, Initializable {

	private Long groupId;
	private String groupType;
    private String searchId;    
    private String searchName;
    private String searchNameFilterType;
    private boolean showConfiguredOnly;
    private boolean showInactive;

    private boolean init;

    public GroupConfigFilterForm(Long groupId) {
    	this.groupId = groupId;
    }

    public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}
	
	public String getGroupType() {
        return this.groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
    
    public String getGroupAssocCd() {
    	return getGroupType().equals(RefCodeNames.GROUP_TYPE_CD.USER) ? RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP 
        		: RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP;
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
	
	public void setSearchNameFilterType(String searchNameFilterType) {
		this.searchNameFilterType = searchNameFilterType;
	}

	public String getSearchNameFilterType() {
		return searchNameFilterType;
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
        this.searchId = null;
        this.searchName = null;
        this.searchNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
        this.showInactive = false;
    }
	
}
