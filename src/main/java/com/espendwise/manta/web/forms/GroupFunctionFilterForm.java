package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupFunctionFilterFormValidator;


@Validation(GroupFunctionFilterFormValidator.class)
public class GroupFunctionFilterForm extends WebForm implements Resetable, Initializable {

    private String functionId;
    private Long groupId;
    private String functionName;
    private String functionType;
    private String functionNameFilterType;
    private Boolean showConfiguredOnly;

    private boolean init;

    public GroupFunctionFilterForm(Long groupId) {
    	this.groupId = groupId;
    }

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFunctionType() {
		return functionType;
	}
	
	public void setFunctionNameFilterType(String functionNameFilterType) {
		this.functionNameFilterType = functionNameFilterType;
	}

	public String getFunctionNameFilterType() {
		return functionNameFilterType;
	}

	public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}

	public Boolean getShowConfiguredOnly() {
		return showConfiguredOnly;
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
        this.functionName = null;
        this.functionType = null;
        this.functionNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
    }
}
