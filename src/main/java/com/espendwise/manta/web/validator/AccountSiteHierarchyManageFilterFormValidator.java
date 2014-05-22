package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.web.forms.AccountSiteHierarchyManageFilterForm;
import com.espendwise.manta.web.util.WebErrors;

public class AccountSiteHierarchyManageFilterFormValidator extends AbstractFormValidator{


    @Override
    public ValidationResult validate(Object obj) {

        AccountSiteHierarchyManageFilterForm valueObj = (AccountSiteHierarchyManageFilterForm) obj;

        WebErrors errors = new WebErrors();

        SimpleFilterFormFieldValidator validator = new SimpleFilterFormFieldValidator(
                "admin.account.siteHierarchy.label.levelName",
                "admin.account.siteHierarchy.label.levelId"
        );

        ValidationResult vr = validator.validate(valueObj);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        return new MessageValidationResult(errors.get());
    }


}
