package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.AccountSiteHierarchyForm;
import com.espendwise.manta.web.forms.SiteHierarchyLevelForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;

public class AccountSiteHierarchyFormValidator extends AbstractFormValidator {


    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        AccountSiteHierarchyForm form = (AccountSiteHierarchyForm) obj;
        boolean levelIsNotSet = false;

        for (SiteHierarchyLevelForm level : form.getLevelCollection()) {

            if (Utility.isSet(level.getName())) {

                if (levelIsNotSet) {
                    errors.clear();
                    errors.putError(new WebError("validation.web.error.siteHierarchy.error.noParentHierarhy"));
                    break;
                }

                TextValidator nameValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.NAME_LENGTH);
                CodeValidationResult vr = nameValidator.validate(level.getName(), new TextErrorWebResolver("admin.account.siteHierarchy.label.siteHierarchyName"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }

            } else {

                levelIsNotSet = true;

            }


        }

        return new MessageValidationResult(errors.get());

    }
}
