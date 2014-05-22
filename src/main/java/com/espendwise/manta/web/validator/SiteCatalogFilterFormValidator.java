package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.SiteCatalogFilterForm;
import com.espendwise.manta.web.forms.SiteUserFilterForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class SiteCatalogFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        SiteCatalogFilterForm form = (SiteCatalogFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        
        ValidationResult vr;
        if (Utility.isSet(form.getCatalogName())) {
            vr = shortDescValidator.validate(form.getCatalogName(), new TextErrorWebResolver("admin.site.catalog.label.catalogName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }

}
