package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.SimpleFilterFormValidator;

@Validation(SimpleFilterFormValidator.class)
public abstract class SimpleConfigurationFilterForm extends SimpleFilterForm {

    private Boolean showConfiguredOnly;

    public Boolean getShowConfiguredOnly() {
        return showConfiguredOnly;
    }

    public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
        this.showConfiguredOnly = showConfiguredOnly;
    }

}