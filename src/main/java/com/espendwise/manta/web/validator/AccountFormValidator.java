package com.espendwise.manta.web.validator;


import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractAddressValidationCodeResolver;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.AccountForm;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.resolver.EmailAddressErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class AccountFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(AccountFormValidator.class);

    public AccountFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        AccountForm valueObj = (AccountForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);

        vr = shortDescValidator.validate(valueObj.getAccountName(), new TextErrorWebResolver("admin.account.label.accountName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getAccountStatus(), new TextErrorWebResolver("admin.account.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getAccountType(), new TextErrorWebResolver("admin.account.label.accountType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getAccountBudgetType(), new TextErrorWebResolver("admin.account.label.budgetType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }


        AccountBillingAddressValidationCodeResolver billingAddressWebErrorResolver = new AccountBillingAddressValidationCodeResolver();
        AddressValidator addressValidator = Validators.getAddressValidator(isStateRequred(valueObj.getBillingAddress()));
        vr = addressValidator.validate(valueObj.getBillingAddress(), billingAddressWebErrorResolver);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        AccountPrimaryContactValidationCodeResolver primaryContactWebErrorReslover = new AccountPrimaryContactValidationCodeResolver();
        ContactValidator contactValidator = Validators.getContactValidator(isStateRequred(valueObj.getPrimaryContact().getAddress()));
        vr = contactValidator.validate(valueObj.getPrimaryContact(), primaryContactWebErrorReslover);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        EmailAddressValidator emailValidator = Validators.getEmailAddressValidator(true);
    	if (valueObj.getPrimaryContact().getEmail() != null) {
            vr = emailValidator.validate(valueObj.getPrimaryContact().getEmail(),
                            new EmailAddressErrorWebResolver("admin.account.label.primaryContact.email")) ;
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        return new MessageValidationResult(errors.get());
    }

    private boolean isStateRequred(AddressInputForm billingAddress) {
        return AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getIsStateRequiredForCountry(billingAddress.getCountryCd());
    }


    private class AccountPrimaryContactValidationCodeResolver extends AbstractContactValidationCodeResolver {

        protected ArgumentedMessage isStatePrivinceMustBeEmpty(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.variableMustBeEmpty",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.primaryContact",
                                    Args.i18nTyped("admin.account.label.primaryContact.address.state")
                            )
                    )

            );
        }

        protected ArgumentedMessage isCityRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.city")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isPostalCodeRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.postalCode")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isStateRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.state")
                                    )
                            ), maxLength)

            );
        }


        protected ArgumentedMessage isFirstNameRangeOu(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.firstName")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isLastNameRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.lastName")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isEmailRangeOu(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.email")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isFaxRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.fax")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isMobileRangeOu(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.mobile")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isTelephoneRangeOu(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.telephone")
                                    )
                            ), maxLength)

            );
        }

        protected ArgumentedMessage isAddress1RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.address1")
                                    )
                            ), maxLength)
            );
        }

        protected ArgumentedMessage isAddress2RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.address2")
                                    )
                            ), maxLength)
            );
        }

        protected ArgumentedMessage isAddress3RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.primaryContact",
                                            Args.i18nTyped("admin.account.label.primaryContact.address.address3")
                                    )
                            ), maxLength)
            );
        }

        protected ArgumentedMessage isStateNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.primaryContact",
                                    Args.i18nTyped("admin.account.label.primaryContact.address.state")
                            )
                    )
            );
        }

        @Override
        protected ArgumentedMessage isNotSet(String field, ValidationCode code) {
            if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateNotSet(code);
            }
            return null;
        }

        @Override
        protected ArgumentedMessage isRangeOut(String field, ValidationCode code) {

            if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS2.equalsIgnoreCase(field)) {
                return isAddress2RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS3.equalsIgnoreCase(field)) {
                return isAddress3RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.FIRST_NAME.equalsIgnoreCase(field)) {
                return isFirstNameRangeOu(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.LAST_NAME.equalsIgnoreCase(field)) {
                return isLastNameRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.EMAIL.equalsIgnoreCase(field)) {
                return isEmailRangeOu(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.FAX.equalsIgnoreCase(field)) {
                return isFaxRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.MOBILE.equalsIgnoreCase(field)) {
                return isMobileRangeOu(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IContact.TELEPHONE.equalsIgnoreCase(field)) {
                return isTelephoneRangeOu(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            }

            return null;

        }

        @Override
        protected ArgumentedMessage isMustBeEmpty(String field, ValidationCode code) {
            if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStatePrivinceMustBeEmpty(code);
            }
            return null;
        }
    }


    private class AccountBillingAddressValidationCodeResolver extends AbstractAddressValidationCodeResolver {

        protected ArgumentedMessage isStatePrivinceMustBeEmpty(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.variableMustBeEmpty",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.state")
                            )
                    )

            );
        }

        protected ArgumentedMessage isCityRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.city")
                            )
                    ), maxLength)

            );
        }

        protected ArgumentedMessage isPostalCodeRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.postalCode")
                            )
                    ), maxLength)

            );
        }

        protected ArgumentedMessage isStateRangeOut(ValidationCode code, String validationValue, Number maxLength) {
            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.state")
                            )
                    ), maxLength)

            );
        }


        protected ArgumentedMessage isAddress1RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(
                            new MessageI18nArgument(
                                    new ArgumentedMessageImpl(
                                            "admin.account.label.billingAddress",
                                            Args.i18nTyped("admin.account.label.billingAddress.address.address1")
                                    )
                            ), maxLength)
            );
        }

        protected ArgumentedMessage isAddress2RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.address2")
                            )
                    ), maxLength)
            );
        }

        protected ArgumentedMessage isAddress3RangeOut(ValidationCode code, String validationValue, Number maxLength) {

            return new ArgumentedMessageImpl("validation.web.error.stringRangeOut",
                    Args.i18nTyped(new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.address3")
                            )
                    ), maxLength)
            );
        }

        protected ArgumentedMessage isCountryNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.country")
                            )
                    )
            );
        }

        protected ArgumentedMessage isCityNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.city")
                            )
                    )
            );
        }

        protected ArgumentedMessage isAddress1NotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.address1")
                            )
                    )
            );
        }

        protected ArgumentedMessage isStateNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.state")
                            )
                    )
            );
        }

        protected ArgumentedMessage isPostalCodeNotSet(ValidationCode code) {
            return new ArgumentedMessageImpl("validation.web.error.emptyValue",
                    new MessageI18nArgument(
                            new ArgumentedMessageImpl(
                                    "admin.account.label.billingAddress",
                                    Args.i18nTyped("admin.account.label.billingAddress.address.postalCode")
                            )
                    )
            );
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
        protected ArgumentedMessage isRangeOut(String field, ValidationCode code) {

            if (IAddress.ADDRESS1.equalsIgnoreCase(field)) {
                return isAddress1RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS2.equalsIgnoreCase(field)) {
                return isAddress2RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.ADDRESS3.equalsIgnoreCase(field)) {
                return isAddress3RangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.CITY.equalsIgnoreCase(field)) {
                return isCityRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.POSTAL_CODE.equalsIgnoreCase(field)) {
                return isPostalCodeRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            } else if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStateRangeOut(code, (String) code.getArguments()[1].get(), (Number) code.getArguments()[2].get());
            }

            return null;

        }

        @Override
        protected ArgumentedMessage isMustBeEmpty(String field, ValidationCode code) {
            if (IAddress.STATE_PROVINCE_CD.equalsIgnoreCase(field)) {
                return isStatePrivinceMustBeEmpty(code);
            }
            return null;
        }
    }


}
