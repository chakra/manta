package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateUserFilterFormValidator;

import java.util.List;

@Validation(LocateUserFilterFormValidator.class)
public class LocateUserFilterForm extends WebForm implements Resetable, Initializable {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userType;
    
    private String userNameFilterType = Constants.FILTER_TYPE.START_WITH;

    private Boolean showInactive;

    private boolean init;

    public LocateUserFilterForm() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserNameFilterType() {
        return userNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setUserNameFilterType(String userNameFilterType) {
        this.userNameFilterType = userNameFilterType;
    }


    @Override
    public void reset() {
        this.userNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.userId = null;
        this.userName = null;
        this.firstName = null;
        this.lastName = null;
        this.userType = null;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

}
