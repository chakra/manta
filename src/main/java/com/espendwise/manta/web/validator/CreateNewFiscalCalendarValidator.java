package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.AccountFiscalCalendarForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class CreateNewFiscalCalendarValidator  extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(CreateNewFiscalCalendarValidator.class);

    public CreateNewFiscalCalendarValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        AccountFiscalCalendarForm form = (AccountFiscalCalendarForm) obj;



        IntegerValidator intValidator = Validators.getIntegerValidator();

        CodeValidationResult vr = intValidator.validate(form.getNumberOfBudgetPeriods(), new NumberErrorWebResolver("admin.account.fiscalCalendar.label.numberOfPeriods"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        if (intValidator.getParsedValue() != null && intValidator.getParsedValue() == 0) {
            errors.putError(new WebError("validation.web.error.mustBeGreaterThanZero", Args.i18nTyped("admin.account.fiscalCalendar.label.numberOfPeriods")));
        }

        if (intValidator.getParsedValue() != null && intValidator.getParsedValue() > Constants.VALIDATION_FIELD_CRITERIA.MAX_FISCAL_MONTH) {
            errors.putError(
                    new WebError(
                            "validation.web.error.maximumValueOfField",
                            new StringI18nArgument("admin.account.fiscalCalendar.label.numberOfPeriods"),
                            new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.MAX_FISCAL_MONTH))
            );
        }


        return new MessageValidationResult(errors.get());

    }
}