package com.espendwise.manta.util.validation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;

public class ContactValidator implements CustomValidator<IContact, AbstractContactValidationCodeResolver> {

    private boolean stateProvinceRequired;

    public ContactValidator(boolean stateProvinceRequired) {
        this.stateProvinceRequired = stateProvinceRequired;
    }

    public boolean isStateProvinceRequired() {
        return stateProvinceRequired;
    }

    @Override
    public CodeValidationResult validate(IContact contact, AbstractContactValidationCodeResolver resolver) throws ValidationException {

        List<ValidationCode> errorCodes = new ArrayList<ValidationCode>();

        AddressValidator addressValidator = Validators.getAddressValidator(isStateProvinceRequired());
        CodeValidationResult vr = addressValidator.validate(contact.getAddress());
        if (vr != null) {
            errorCodes.addAll(Arrays.asList(vr.getValidationCodes()));
        }

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator telephoneNumberValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH);

        vr = shortDescValidator.validate(contact.getFirstName());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.FIRST_NAME),
                                new StringArgument(contact.getFirstName()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = shortDescValidator.validate(contact.getLastName());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.LAST_NAME),
                                new StringArgument(contact.getLastName()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = shortDescValidator.validate(contact.getEmail());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.EMAIL),
                                new StringArgument(contact.getEmail()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = telephoneNumberValidator.validate(contact.getTelephone());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.TELEPHONE),
                                new StringArgument(contact.getTelephone()) ,
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                )

                );
            }
        }

        vr = telephoneNumberValidator.validate(contact.getFax());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.FAX),
                                new StringArgument(contact.getFax()) ,
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                        )

                );
            }
        }

        vr = telephoneNumberValidator.validate(contact.getMobile());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.MOBILE),
                                new StringArgument(contact.getMobile()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                        )

                );
            }
        }

        return new CodeValidationResult(resolver, errorCodes.toArray(new ValidationCode[errorCodes.size()]));

    }

    @Override
    public CodeValidationResult validate(IContact obj) throws ValidationException {
        return validate(obj, null);
    }
}
