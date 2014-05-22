package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.SimpleFilterFormValidator;

@Validation(SimpleFilterFormValidator.class)
public abstract class SimpleFilterForm extends AbstractSimpleFilterForm {

    public abstract String getFilterKey();
    public  String getFilterIdKey(){return "";};

}
