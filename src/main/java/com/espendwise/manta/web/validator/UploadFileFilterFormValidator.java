package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.web.forms.UploadFileFilterForm;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;

public class UploadFileFilterFormValidator extends AbstractFormValidator {

    public UploadFileFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        UploadFileFilterForm form = (UploadFileFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getUploadId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getUploadId(), new SearchByIdErrorResolver("admin.global.filter.label.uploadId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getUploadFileName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getUploadFileName(), new TextErrorWebResolver("admin.global.filter.label.uploadFileName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        String datePattern = AppI18nUtil.getDatePattern();

        if (Utility.isSetIgnorePattern(form.getAddDate(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            ValidationResult vr = dateValidator.validate(form.getAddDate(), new DateErrorWebResolver("admin.global.filter.label.uploadAddDate", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSetIgnorePattern(form.getModifiedDate(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            ValidationResult vr = dateValidator.validate(form.getModifiedDate(), new DateErrorWebResolver("admin.global.filter.label.uploadModifiedDate", "Date Posted To"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }
}
