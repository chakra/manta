package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.ProfilePasswordMgrFormValidator;

@Validation(ProfilePasswordMgrFormValidator.class)
public class ProfilePasswordMgrForm extends WebForm implements Initializable {
    private boolean allowChangePassword;
	private boolean reqPasswordResetUponInitLogin;
    private boolean reqPasswordResetInDays;
    private String expiryInDays;
    private String notifyExpiryInDays = "7";
    
    private boolean init;

	@Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

	public void setAllowChangePassword(boolean allowChangePassword) {
		this.allowChangePassword = allowChangePassword;
	}

	public boolean isAllowChangePassword() {
		return allowChangePassword;
	}

	public void setReqPasswordResetUponInitLogin(
			boolean reqPasswordResetUponInitLogin) {
		this.reqPasswordResetUponInitLogin = reqPasswordResetUponInitLogin;
	}

	public boolean isReqPasswordResetUponInitLogin() {
		return reqPasswordResetUponInitLogin;
	}

	public void setReqPasswordResetInDays(boolean reqPasswordResetInDays) {
		this.reqPasswordResetInDays = reqPasswordResetInDays;
	}

	public boolean isReqPasswordResetInDays() {
		return reqPasswordResetInDays;
	}

	public void setExpiryInDays(String expiryInDays) {
		this.expiryInDays = expiryInDays;
	}

	public String getExpiryInDays() {
		return expiryInDays;
	}

	public void setNotifyExpiryInDays(String notifyExpiryInDays) {
		this.notifyExpiryInDays = notifyExpiryInDays;
	}

	public String getNotifyExpiryInDays() {
		return notifyExpiryInDays;
	}
	
	@Override
    public String toString() {
        return "ProfilePasswordMgrForm{" +
                ", allowChangePassword=" + allowChangePassword +
                ", reqPasswordResetUponInitLogin='" + reqPasswordResetUponInitLogin + '\'' +
                ", reqPasswordResetInDays='" + reqPasswordResetInDays + '\'' +
                ", expiryInDays='" + expiryInDays + '\'' +
                ", notifyExpiryInDays='" + notifyExpiryInDays + '\'' +
                ", init=" + init +
                '}';
    }
}
