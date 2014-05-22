package com.espendwise.manta.web.validator;


import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.spi.IUserContact;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.EmailAddressValidator;
import com.espendwise.manta.util.validation.LongValidator;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.UserContactValidator;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;
import com.espendwise.manta.util.validation.rules.UserUpdateConstraint;
import com.espendwise.manta.util.validation.rules.UserUpdateMainConstraint;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.forms.UserForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.EmailAddressErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class UserFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(UserFormValidator.class);

    public UserFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        UserForm valueObj = (UserForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
        LongValidator longValidator = Validators.getLongValidator();
        EmailAddressValidator multipleAddressEmailValidator = Validators.getEmailAddressValidator(true);
        EmailAddressValidator emailValidator = Validators.getEmailAddressValidator();

        vr = shortDescValidator.validate(valueObj.getUserLogonName(), new TextErrorWebResolver("admin.user.label.userName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        //vr = shortDescValidator.validate(valueObj.getUserStatus(), new TextErrorWebResolver("admin.user.label.status"));
        //if (vr != null) {
        //    errors.putErrors(vr.getResult());
        //}

        vr = shortDescValidator.validate(valueObj.getUserType(), new TextErrorWebResolver("admin.user.label.type"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        if (!Utility.isSetIgnorePattern(valueObj.getUserActiveDate(), AppI18nUtil.getDatePatternPrompt())) {
            errors.putMessage("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.userActiveDate"));
        }
        
        if (Utility.isSetIgnorePattern(valueObj.getUserActiveDate(), AppI18nUtil.getDatePatternPrompt())) {
            vr = dateValidator.validate(valueObj.getUserActiveDate(), new DateErrorWebResolver("admin.user.label.userActiveDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSetIgnorePattern(valueObj.getUserInactiveDate(), AppI18nUtil.getDatePatternPrompt())) {
            vr = dateValidator.validate(valueObj.getUserInactiveDate(), new DateErrorWebResolver("admin.user.label.userInactiveDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (!Utility.isSet(valueObj.getUserLanguage())) {
            errors.putMessage("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.preferredLanguage"));
        }

        if (!Utility.isSet(valueObj.getUserStatus())) {
            errors.putMessage("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.status"));
        }
        
        // If creating new user...
        if (valueObj.getUserId() == 0) {
            // password and confirm password must both be set and equal
            vr = shortDescValidator.validate(valueObj.getUserPassword(), new TextErrorWebResolver("admin.user.label.newPassword"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            
            vr = shortDescValidator.validate(valueObj.getUserConfirmPassword(), new TextErrorWebResolver("admin.user.label.confirmPassword"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            
            // check if the password and confirm password are equal
            if (Utility.isSet(valueObj.getUserPassword()) && Utility.isSet(valueObj.getUserConfirmPassword())) {
                if (!valueObj.getUserPassword().equals(valueObj.getUserConfirmPassword())) {
                    errors.putMessage("validation.web.error.fieldsNotEqual", Args.i18nTyped("admin.user.label.newPassword", "admin.user.label.confirmPassword"));
                }
            }
            
        } else { // updating user
            // if password set, then 'password' must equal 'confirm password'
            if (Utility.isSet(valueObj.getUserPassword()) || Utility.isSet(valueObj.getUserConfirmPassword())) {
                vr = shortDescValidator.validate(valueObj.getUserPassword(), new TextErrorWebResolver("admin.user.label.newPassword"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }

                vr = shortDescValidator.validate(valueObj.getUserConfirmPassword(), new TextErrorWebResolver("admin.user.label.confirmPassword"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
                // check if the password and confirm password are equal
                if (Utility.isSet(valueObj.getUserPassword()) && Utility.isSet(valueObj.getUserConfirmPassword())) {
                    if (!valueObj.getUserPassword().equals(valueObj.getUserConfirmPassword())) {
                        errors.putMessage("validation.web.error.fieldsNotEqual", Args.i18nTyped("admin.user.label.newPassword", "admin.user.label.confirmPassword"));
                    }
                }
            }
        }

        if (Utility.isSet(valueObj.getUserContact().getContact().getEmail())) {
            vr = multipleAddressEmailValidator.validate(valueObj.getUserContact().getContact().getEmail(),
                                         new EmailAddressErrorWebResolver("admin.user.label.email"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getUserContact().getEscalationEmail())) {
            vr = multipleAddressEmailValidator.validate(valueObj.getUserContact().getEscalationEmail(),
                                         new EmailAddressErrorWebResolver("admin.user.label.escalationEmail"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getUserContact().getTextingAddress())) {
            vr = emailValidator.validate(valueObj.getUserContact().getTextingAddress(),
                                         new EmailAddressErrorWebResolver("admin.user.label.textingAddress"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        
        // ========== IVR validating
        if (Utility.isSet(valueObj.getIvrUserIdentificationNumber())) {
            vr = longValidator.validate(valueObj.getIvrUserIdentificationNumber(), new NumberErrorWebResolver("admin.user.label.userIdentificationNumber"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            if (valueObj.getIvrUserIdentificationNumber().trim().length() < 10) {
                errors.putMessage("validation.web.error.stringRangeLess", Args.i18nTyped("admin.user.label.userIdentificationNumber", 10));
            }
            if (valueObj.getIvrUserIdentificationNumber().trim().length() > 15) {
                errors.putMessage("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.userIdentificationNumber", 15));
            }
        }
        
        if (Utility.isSet(valueObj.getIvrPIN()) && !Utility.isSet(valueObj.getIvrUserIdentificationNumber())) {
            errors.putMessage("validation.web.error.fieldsRequiredForField", Args.i18nTyped("admin.user.label.userIdentificationNumber", "admin.user.label.PIN"));
        }
        
        if (Utility.isSet(valueObj.getIvrConfirmPIN()) && !Utility.isSet(valueObj.getIvrUserIdentificationNumber())) {
            errors.putMessage("validation.web.error.fieldRequiredForField", Args.i18nTyped("admin.user.label.userIdentificationNumber", "admin.user.label.confirmPIN"));
        }
        
        if (Utility.isSet(valueObj.getIvrPIN())) {
            if (valueObj.getIvrPIN().trim().length() < 4) {
                errors.putMessage("validation.web.error.stringRangeLess", Args.i18nTyped("admin.user.label.PIN", Integer.valueOf(4)));
            }
            if (valueObj.getIvrPIN().trim().length() > 15) {
                errors.putMessage("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.PIN", Integer.valueOf(4)));
            }
        }
        
        if (Utility.isSet(valueObj.getIvrUserIdentificationNumber()) && !Utility.isSet(valueObj.getIvrPIN())) {
            errors.putMessage("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.PIN"));
        }
        
        if (Utility.isSet(valueObj.getIvrPIN()) && !Utility.isSet(valueObj.getIvrConfirmPIN())) {
            errors.putMessage("validation.web.error.fieldRequiredForField", Args.i18nTyped("admin.user.label.PIN", "admin.user.label.confirmPIN"));
        }
        
        if (Utility.isSet(valueObj.getIvrUserIdentificationNumber()) && Utility.isSet(valueObj.getIvrConfirmPIN())) {
            if (!valueObj.getIvrConfirmPIN().equals(valueObj.getIvrPIN())) {
                errors.putMessage("validation.web.error.fieldsNotEqual", Args.i18nTyped("admin.user.label.PIN", "admin.user.label.confirmPIN"));
            }
        }
        
        List<AllStoreIdentificationView> selectedEntities = (List<AllStoreIdentificationView>)valueObj.getEntities().getSelected();
        if (!Utility.isSet(selectedEntities)) {
            errors.putMessage("validation.web.error.atLeastOneOf", Args.i18nTyped("admin.user.label.primaryEntity"));
        } else {
            if (!Utility.isSet(valueObj.getDefaultStoreId())) {
                errors.putMessage("validation.web.error.mustBeSet", Args.i18nTyped("admin.user.label.defaultEntity"));
            } else {
                boolean defaultPresentInSelected = false;
                Long defaultStoreId = Long.valueOf(valueObj.getDefaultStoreId());
                if (Utility.isSet(selectedEntities)) {
                    for (AllStoreIdentificationView view : selectedEntities) {
                        if (view.getMainStoreId().equals(defaultStoreId)) {
                            defaultPresentInSelected = true;
                            break;
                        }
                    }
                }
                if (!defaultPresentInSelected) {
                    errors.putMessage("validation.web.error.paramMustBeInSelected", Args.i18nTyped("admin.user.label.defaultEntity", "admin.user.label.primaryEntity"));
                }
            }
        }
        
        /*
        AccountBillingAddressValidationCodeResolver billingAddressWebErrorResolver = new AccountBillingAddressValidationCodeResolver();
        AddressValidator addressValidator = Validators.getAddressValidator(isStateRequred(valueObj.getBillingAddress()));
        vr = addressValidator.validate(valueObj.getBillingAddress(), billingAddressWebErrorResolver);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        */
        UserPrimaryContactValidationCodeResolver userContactWebErrorReslover = new UserPrimaryContactValidationCodeResolver();
        UserContactValidator userContactValidator = Validators.getUserContactValidator(isStateRequred(valueObj.getUserContact().getContact().getAddress()));
        vr = userContactValidator.validate(valueObj.getUserContact(), userContactWebErrorReslover);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        return new MessageValidationResult(errors.get());
    }

    private boolean isStateRequred(AddressInputForm address) {
        return AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getIsStateRequiredForCountry(address.getCountryCd());
    }


    private class  UserPrimaryContactValidationCodeResolver  extends AbstractContactValidationCodeResolver {

        protected ArgumentedMessage isWrongNumberFormat(String s, ValidationCode code) { return null; }
        protected ArgumentedMessage isInvalidPositiveValue(String s, ValidationCode code) {  return null; }

        protected ArgumentedMessage isStateProvinceMustBeEmpty(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.variableMustBeEmpty", Args.i18nTyped("admin.user.label.stateProvince"));
        }
        
        @Override
        protected ArgumentedMessage isMustBeEmpty(String field, ValidationCode code) {
            if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateProvinceMustBeEmpty(code);
            }
            return null;
        }
        
        @Override
        protected ArgumentedMessage isNotSet(String field, ValidationCode code) {
            if (IContact.FIRST_NAME.equalsIgnoreCase(field)) {
                return isFirstNameNotSet(code);
            } else if (IContact.LAST_NAME.equalsIgnoreCase(field)) {
                return isLastNameNotSet(code);
            } else if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1NotSet(code);
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityNotSet(code);
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeNotSet(code);
            } else if (IContact.TELEPHONE.equalsIgnoreCase(field)) {
                return isPhoneNotSet(code);
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateNotSet(code);
            } else if (IAddress.COUNTRY_CD.equalsIgnoreCase(field)) {
                return isCountryNotSet(code);
            }

            return null;
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
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateProvinceRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.TELEPHONE.equalsIgnoreCase(field)) {
                return isPhoneRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.FAX.equalsIgnoreCase(field))  {
                return isFaxRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.MOBILE.equalsIgnoreCase(field)) {
                return isMobileRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.EMAIL.equalsIgnoreCase(field)) {
                return isEmailRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IUserContact.ESCALATION_EMAIL.equalsIgnoreCase(field)) {
                return isEscalationEmailRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IUserContact.TEXTING_ADDRESS.equalsIgnoreCase(field)) {
                return isTextingAddressRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            }
            return null;

        }
        
        protected ArgumentedMessage isFirstNameRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.firstName", maxLength));
        }
        
        protected ArgumentedMessage isLastNameRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.lastName", maxLength));
        }
        
        protected ArgumentedMessage isCityRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.city", maxLength));
        }

        protected ArgumentedMessage isAddress1RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.address1", maxLength));
        }
        
        protected ArgumentedMessage isAddress2RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.address2", maxLength));
        }

        protected ArgumentedMessage isPostalCodeRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.postalCode", maxLength));
        }

        protected ArgumentedMessage isStateProvinceRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.stateProvince", maxLength));
        }

        protected ArgumentedMessage isPhoneRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.phone", maxLength));
        }

        protected ArgumentedMessage isFaxRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.fax", maxLength));
        }

        protected ArgumentedMessage isMobileRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.mobile", maxLength));
        }

        protected ArgumentedMessage isEmailRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.email", maxLength));
        }

        protected ArgumentedMessage isEscalationEmailRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.escalationEmail", maxLength));
        }

        protected ArgumentedMessage isTextingAddressRangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Args.i18nTyped("admin.user.label.textingAddress", maxLength));
        }

        protected ArgumentedMessage isFirstNameNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.firstName"));
        }
        
        protected ArgumentedMessage isLastNameNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.lastName"));
        }
        
        protected ArgumentedMessage isCityNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.city"));
        }

        protected ArgumentedMessage isAddress1NotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(new ArgumentedMessageImpl("admin.user.label.address1")));
        }

        protected ArgumentedMessage isStateNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.stateProvince"));
        }
        
        protected ArgumentedMessage isCountryNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.country"));
        }

        protected ArgumentedMessage isPostalCodeNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.postalCode"));
        }
        
        protected ArgumentedMessage isPhoneNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.user.label.phone"));
        }   
    }

}
