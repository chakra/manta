package com.espendwise.manta.web.validator;


import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.forms.SiteForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class SiteFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        SiteForm valueObj = (SiteForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);

        vr = shortDescValidator.validate(valueObj.getSiteName(), new TextErrorWebResolver("admin.site.label.siteName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getStatus(), new TextErrorWebResolver("admin.site.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        if (valueObj.getIsNew()) {
            IntegerValidator integerValidator = Validators.getIntegerValidator();
            vr = integerValidator.validate(valueObj.getAccountId(), new NumberErrorWebResolver("admin.site.label.account"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSetIgnorePattern(valueObj.getEffDate(), AppI18nUtil.getDatePatternPrompt())) {

            DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
            vr = dateValidator.validate(valueObj.getEffDate(), new DateErrorWebResolver("admin.site.label.effDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }

        }

        if (Utility.isSetIgnorePattern(valueObj.getExpDate(), AppI18nUtil.getDatePatternPrompt())) {

            DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
            vr = dateValidator.validate(valueObj.getExpDate(), new DateErrorWebResolver("admin.site.label.expDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        SiteContactValidationCodeResolver contactWebErrorResolver = new SiteContactValidationCodeResolver();
        ContactValidator contactValidator = Validators.getContactValidator(isStateRequired(valueObj.getContact().getAddress()));
        vr = contactValidator.validate(valueObj.getContact(), contactWebErrorResolver);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        return new MessageValidationResult(errors.get());

    }

    private boolean isStateRequired(AddressInputForm address) {
        return AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getIsStateRequiredForCountry(address.getCountryCd());
    }

    private class SiteContactValidationCodeResolver extends AbstractContactValidationCodeResolver {


        private ArgumentedMessage isAddress1RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.address1", maxLength));
        }

        private ArgumentedMessage isAddress2RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.address2", maxLength));
        }

        private ArgumentedMessage isAddress3RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.address3", maxLength));
        }

        private ArgumentedMessage isAddress4RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.address4", maxLength));
        }

        private ArgumentedMessage isCityRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.city", maxLength));
        }

        private ArgumentedMessage isPostalCodeRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.postalCode", maxLength));
        }

        private ArgumentedMessage isStateRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.state", maxLength));
        }

        private ArgumentedMessage isFirstNameRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.firstName", maxLength));
        }

        private ArgumentedMessage isLastNameRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.address.lastName", maxLength));
        }

        private ArgumentedMessage isTelephoneRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.site.label.contact.telephone", maxLength));
        }

        private ArgumentedMessage isAddress1NotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.contact.address.address1"));
        }

        private ArgumentedMessage isCityNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.contact.address.city"));
        }

        private ArgumentedMessage isCountryNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.contact.address.country"));
        }

        private ArgumentedMessage isPostalCodeNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.contact.address.postalCode"));
        }

        private ArgumentedMessage isStateNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.contact.address.state"));
        }

        protected ArgumentedMessage isStateProvinceMustBeEmpty(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.variableMustBeEmpty", Args.i18nTyped("admin.site.label.contact.address.state"));
        }

        @Override
        protected ArgumentedMessage isRangeOut(String field, ValidationCode code) {

            if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS2.equalsIgnoreCase(field)) {
                return isAddress2RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS3.equalsIgnoreCase(field)) {
                return isAddress3RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS4.equalsIgnoreCase(field)) {
                return isAddress4RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.FIRST_NAME.equalsIgnoreCase(field)) {
                return isFirstNameRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.LAST_NAME.equalsIgnoreCase(field)) {
                return isLastNameRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.TELEPHONE.equalsIgnoreCase(field)) {
                return isTelephoneRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            }

            return null;
        }


        @Override
        protected ArgumentedMessage isNotSet(String field, ValidationCode code) {
            if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1NotSet(code);
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityNotSet(code);
            } else if (IAddress.COUNTRY_CD.equalsIgnoreCase(field)) {
                return isCountryNotSet(code);
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeNotSet(code);
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateNotSet(code);
            }

            return null;
        }


        @Override
        protected ArgumentedMessage isMustBeEmpty(String field, ValidationCode code) {
            if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateProvinceMustBeEmpty(code);
            }
            return null;
        }
    }
}
