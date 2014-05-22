package com.espendwise.manta.web.validator;


import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.ContactValidator;
import com.espendwise.manta.util.validation.EmailAddressValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.forms.DistributorForm;
import com.espendwise.manta.web.resolver.EmailAddressErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class DistributorFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        DistributorForm valueObj = (DistributorForm) obj;

        ValidationResult vr;

        TextValidator adminNameValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH, true);
        TextValidator propertyValueValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.BIG_TEXT_LENGTH, false);
        TextValidator statusValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH, true);
        TextValidator localeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH, false);

        //distributor admin name validation
        vr = adminNameValidator.validate(valueObj.getDistributorName(), new TextErrorWebResolver("admin.distributor.label.name"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor display name validation
        vr = propertyValueValidator.validate(valueObj.getDistributorDisplayName(), new TextErrorWebResolver("admin.distributor.label.displayName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor type validation
        vr = propertyValueValidator.validate(valueObj.getDistributorType(), new TextErrorWebResolver("admin.distributor.label.type"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor status validation
        vr = statusValidator.validate(valueObj.getDistributorStatus(), new TextErrorWebResolver("admin.distributor.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor call center hours validation
        vr = propertyValueValidator.validate(valueObj.getDistributorCallCenterHours(), new TextErrorWebResolver("admin.distributor.label.callCenterHours"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor locale validation
        vr = localeValidator.validate(valueObj.getDistributorLocale(), new TextErrorWebResolver("admin.distributor.label.locale"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor company code validation
        vr = propertyValueValidator.validate(valueObj.getDistributorCompanyCode(), new TextErrorWebResolver("admin.distributor.label.companyCode"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //distributor customer reference code validation
        vr = propertyValueValidator.validate(valueObj.getDistributorCustomerReferenceCode(), new TextErrorWebResolver("admin.distributor.label.customerReferenceCode"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        //contact information validation
        DistributorContactValidationCodeResolver contactWebErrorResolver = new DistributorContactValidationCodeResolver();
        ContactValidator contactValidator = Validators.getContactValidator(isStateRequired(valueObj.getContact().getAddress()));
        vr = contactValidator.validate(valueObj.getContact(), contactWebErrorResolver);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        //since the contact validator doesn't handle this, check to make sure the email address has a valid format
        if (valueObj.getContact().getEmail() != null) {
        	EmailAddressValidator emailValidator = Validators.getEmailAddressValidator();
        	vr = emailValidator.validate(valueObj.getContact().getEmail(),
                new EmailAddressErrorWebResolver("admin.distributor.label.contact.email"));
        	if (vr != null) {
        		errors.putErrors(vr.getResult());
        	}
        }

        return new MessageValidationResult(errors.get());

    }

    private boolean isStateRequired(AddressInputForm address) {
        return AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getIsStateRequiredForCountry(address.getCountryCd());
    }

    private class DistributorContactValidationCodeResolver extends AbstractContactValidationCodeResolver {

        private ArgumentedMessage isFirstNameRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.firstName", maxLength));
        }

        private ArgumentedMessage isLastNameRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.lastName", maxLength));
        }
    	
        private ArgumentedMessage isAddress1RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.address1", maxLength));
        }

        private ArgumentedMessage isAddress2RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.address2", maxLength));
        }

        private ArgumentedMessage isAddress3RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.address3", maxLength));
        }

        private ArgumentedMessage isAddress4RangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.address4", maxLength));
        }

        private ArgumentedMessage isCityRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.city", maxLength));
        }

        private ArgumentedMessage isStateRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.state", maxLength));
        }

        private ArgumentedMessage isPostalCodeRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.postalCode", maxLength));
        }

        private ArgumentedMessage isCountryCodeRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.address.country", maxLength));
        }

        private ArgumentedMessage isTelephoneRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.telephone", maxLength));
        }

        private ArgumentedMessage isFaxRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.fax", maxLength));
        }

        private ArgumentedMessage isEmailRangeOut(ValidationCode code, String s, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.distributor.label.contact.email", maxLength));
        }

        private ArgumentedMessage isAddress1NotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.distributor.label.contact.address.address1"));
        }

        private ArgumentedMessage isCityNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.distributor.label.contact.address.city"));
        }

        private ArgumentedMessage isStateNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.distributor.label.contact.address.state"));
        }

        protected ArgumentedMessage isStateProvinceMustBeEmpty(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.variableMustBeEmpty", Args.i18nTyped("admin.distributor.label.contact.address.state"));
        }

        private ArgumentedMessage isPostalCodeNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.distributor.label.contact.address.postalCode"));
        }

        private ArgumentedMessage isCountryNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.distributor.label.contact.address.country"));
        }

        @Override
        protected ArgumentedMessage isRangeOut(String field, ValidationCode code) {

        	if (IContact.FIRST_NAME.equalsIgnoreCase(field)) {
        		return isFirstNameRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
        	} else if (IContact.LAST_NAME.equalsIgnoreCase(field)) {
        		return isLastNameRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
        	} else if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS2.equalsIgnoreCase(field)) {
                return isAddress2RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS3.equalsIgnoreCase(field)) {
                return isAddress3RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS4.equalsIgnoreCase(field)) {
                return isAddress4RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.COUNTRY_CD.equalsIgnoreCase(field)) {
                return isCountryCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.TELEPHONE.equalsIgnoreCase(field)) {
                return isTelephoneRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.FAX.equalsIgnoreCase(field)) {
            	return isFaxRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.EMAIL.equalsIgnoreCase(field)) {
            	return isEmailRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            }

            return null;
        }


        @Override
        protected ArgumentedMessage isNotSet(String field, ValidationCode code) {
            if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1NotSet(code);
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityNotSet(code);
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateNotSet(code);
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeNotSet(code);
            } else if (IAddress.COUNTRY_CD.equalsIgnoreCase(field)) {
                return isCountryNotSet(code);
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
