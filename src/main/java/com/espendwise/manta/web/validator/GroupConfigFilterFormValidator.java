package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.GroupConfigFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;


public class GroupConfigFilterFormValidator extends AbstractFormValidator {

    public GroupConfigFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

    	GroupConfigFilterForm form = (GroupConfigFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        //determine the label to use in error messages
        String idLabel = null;
        String nameLabel = null;
        String groupType = form.getGroupType();
        
        if (RefCodeNames.GROUP_TYPE_CD.USER.equalsIgnoreCase(groupType)) {
        	idLabel = AppI18nUtil.getMessage("admin.global.filter.label.userId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filter.label.userName");
        }
        else if (RefCodeNames.GROUP_TYPE_CD.ACCOUNT.equalsIgnoreCase(groupType)) {
        	idLabel = AppI18nUtil.getMessage("admin.global.filter.label.accountId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filter.label.accountName");
        }
        else if (RefCodeNames.GROUP_TYPE_CD.DISTRIBUTOR.equalsIgnoreCase(groupType)) {
        	idLabel = AppI18nUtil.getMessage("admin.global.filter.label.distributorId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filter.label.distributorName");
        }
        else if (RefCodeNames.GROUP_TYPE_CD.MANUFACTURER.equalsIgnoreCase(groupType)) {
        	idLabel = AppI18nUtil.getMessage("admin.global.filter.label.manufacturerId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filter.label.manufacturerName");
        }
        else if (RefCodeNames.GROUP_TYPE_CD.STORE.equalsIgnoreCase(groupType)) {
        	idLabel = AppI18nUtil.getMessage("admin.global.filter.label.primaryEntityId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filter.label.primaryEntityName");
        }
        else {
        	idLabel = AppI18nUtil.getMessage("admin.global.filterId");
        	nameLabel = AppI18nUtil.getMessage("admin.global.filterValue");
        }
        
        if (Utility.isSet(form.getSearchId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getSearchId(), new SearchByIdErrorResolver(idLabel, null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSearchName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getSearchName(), new TextErrorWebResolver(nameLabel, null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }
}