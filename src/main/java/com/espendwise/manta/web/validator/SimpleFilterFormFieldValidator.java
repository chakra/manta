package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;
import com.espendwise.manta.web.forms.AbstractSimpleFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class SimpleFilterFormFieldValidator extends AbstractFormFieldValidator  {

    private static final Logger logger = Logger.getLogger(SimpleFilterFormFieldValidator.class);

    public SimpleFilterFormFieldValidator(String fieldKey) {
        super(fieldKey, null, "filterValue");
    }
    public SimpleFilterFormFieldValidator(String fieldKey, String fieldIdKey) {
        super(fieldKey, fieldIdKey, null, "filterValue");
    }

    public SimpleFilterFormFieldValidator(String fieldKey, String fieldIdKey, String fieldName) {
        super(fieldKey, fieldIdKey, fieldName, "filterValue");
    }


    public SimpleFilterFormFieldValidator() {
        super("admin.global.filterValue","admin.global.filterId", "Filter Value", "filterValue");
    }

    public ValidationResult validate(Object obj, ValidationCodeResolver resolver) {

        AbstractSimpleFilterForm valueObj = (AbstractSimpleFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Constants.FILTER_TYPE.ID.equals(valueObj.getFilterType())) {

            try {

                IntegerValidator intValidator = Validators.getIntegerValidator();

                ValidationResult vr;
                if (resolver == null) {
                    vr = intValidator.validate(valueObj.getFilterValue(), new SearchByIdErrorResolver(getFieldKey(), getFieldName()));
                } else {
                    vr = intValidator.validate(valueObj.getFilterValue(), resolver);
                }

                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }

            } catch (ValidationException e) {
                e.printStackTrace();
                throw e;
            }
        } else if(Utility.isSet(valueObj.getFilterId())) {
            try {

                IntegerValidator intValidator = Validators.getIntegerValidator();

                ValidationResult vr;
                if (resolver == null) {
                    vr = intValidator.validate(valueObj.getFilterId(), new SearchByIdErrorResolver(getFieldIdKey(), getFieldName()));
                } else {
                    vr = intValidator.validate(valueObj.getFilterId(), resolver);
                }

                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }

            } catch (ValidationException e) {
                e.printStackTrace();
                throw e;
            }
        } else if(Utility.isSet(valueObj.getFilterValue())) {

            TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);

            ValidationResult vr = shortDescValidator.validate(valueObj.getFilterValue(), new TextErrorWebResolver(getFieldKey(), getFieldName()));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }


    public ValidationResult validate(Object obj) {
        return validate(obj, (ValidationCodeResolver) null);
    }

}
