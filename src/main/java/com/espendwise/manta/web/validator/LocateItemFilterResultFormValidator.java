package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.LocateItemFilterResultForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.FilterResultErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateItemFilterResultFormValidator extends AbstractFormValidator {
    public interface FILTER_RESULT_LIMIT {
        public static final Integer ITEM = 500;
    }

    @Override
    public ValidationResult validate(Object obj) {

        LocateItemFilterResultForm form = (LocateItemFilterResultForm) obj;

        WebErrors errors = new WebErrors();



         if (form.getSelectedItems() != null && Utility.isSet(form.getSelectedItems().getSelectableObjects())) {
            FilterResultValidator validator = Validators.getFilterResultValidator(FILTER_RESULT_LIMIT.ITEM);
            CodeValidationResult vr = validator.validate(new Integer(form.getSelectedItems().getSelectableObjects().size()), new FilterResultErrorWebResolver("admin.global.filter.label.items"));
            
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }


}
