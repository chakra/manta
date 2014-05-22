package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.CorporateScheduleFormValidator;

@Validation(CorporateScheduleFormValidator.class)
public class CorporateScheduleForm extends ScheduleForm {

}
