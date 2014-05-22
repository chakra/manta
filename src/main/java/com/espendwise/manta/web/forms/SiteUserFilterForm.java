package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.SiteUserFilterFormValidator;


@Validation(SiteUserFilterFormValidator.class)
public class SiteUserFilterForm extends WebForm implements Resetable, Initializable {

    private Long siteId;
    private String userId;
    private String userName;
    private String userNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private Boolean showConfiguredOnly;
    private Boolean showInactive;

    private boolean init;

    public SiteUserFilterForm() {
        super();
    }

    public SiteUserFilterForm(Long siteId) {
        super();
        this.siteId = siteId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameFilterType() {
        return userNameFilterType;
    }

    public void setUserNameFilterType(String userNameFilterType) {
        this.userNameFilterType = userNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public Boolean getShowConfiguredOnly() {
        return showConfiguredOnly;
    }

    public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
        this.showConfiguredOnly = showConfiguredOnly;
    }

    @Override
    public void initialize() {
        reset();
        this.userNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.userId = null;
        this.userName = null;
        this.userNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
        this.showInactive = false;
    }

}
