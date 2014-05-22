package com.espendwise.manta.util.validation;


import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.spi.IUserContact;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserContactValidator implements CustomValidator<IUserContact, AbstractContactValidationCodeResolver> {

    private boolean stateProvinceRequired;

    public UserContactValidator(boolean stateProvinceRequired) {
        this.stateProvinceRequired = stateProvinceRequired;
    }

    public boolean isStateProvinceRequired() {
        return stateProvinceRequired;
    }

    @Override
    public CodeValidationResult validate(IUserContact userContact, AbstractContactValidationCodeResolver resolver) throws ValidationException {

        List<ValidationCode> errorCodes = new ArrayList<ValidationCode>();

        AddressValidator addressValidator = Validators.getAddressValidator(isStateProvinceRequired());
        CodeValidationResult vr = addressValidator.validate(userContact.getContact().getAddress());
        if (vr != null) {
            errorCodes.addAll(Arrays.asList(vr.getValidationCodes()));
        }

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator telephoneNumberValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH);

        vr = shortDescValidator.validate(userContact.getContact().getFirstName());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.FIRST_NAME),
                                new StringArgument(userContact.getContact().getFirstName()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = shortDescValidator.validate(userContact.getContact().getLastName());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.LAST_NAME),
                                new StringArgument(userContact.getContact().getLastName()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = shortDescValidator.validate(userContact.getContact().getEmail());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.EMAIL),
                                new StringArgument(userContact.getContact().getEmail()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }
        
        vr = shortDescValidator.validate(userContact.getEscalationEmail());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IUserContact.ESCALATION_EMAIL),
                                new StringArgument(userContact.getEscalationEmail()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }
        
        vr = shortDescValidator.validate(userContact.getTextingAddress());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IUserContact.TEXTING_ADDRESS),
                                new StringArgument(userContact.getTextingAddress()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                        )

                );
            }
        }

        vr = telephoneNumberValidator.validate(userContact.getContact().getTelephone());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.TELEPHONE),
                                new StringArgument(userContact.getContact().getTelephone()) ,
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                )

                );
            }
        }

        vr = telephoneNumberValidator.validate(userContact.getContact().getFax());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.FAX),
                                new StringArgument(userContact.getContact().getFax()) ,
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                        )

                );
            }
        }

        vr = telephoneNumberValidator.validate(userContact.getContact().getMobile());
        if (vr != null && vr.getValidationCodes() != null) {
            for (ValidationCode errorCode : vr.getValidationCodes()) {
                errorCodes.add(
                        new ValidationCodeImpl(
                                errorCode.getReason(),
                                new StringArgument(IContact.MOBILE),
                                new StringArgument(userContact.getContact().getMobile()),
                                new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.TELEPHONE_NUMBER_LENGTH)
                        )

                );
            }
        }

        return new CodeValidationResult(resolver, errorCodes.toArray(new ValidationCode[errorCodes.size()]));

    }

    @Override
    public CodeValidationResult validate(IUserContact obj) throws ValidationException {
        return validate(obj, null);
    }
}
