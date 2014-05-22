package com.espendwise.manta.util.validation;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractAddressValidationCodeResolver;

public class AddressValidator implements CustomValidator<IAddress, AbstractAddressValidationCodeResolver> {

    private static final Logger logger = Logger.getLogger(AddressValidator.class);

    private boolean stateProvinceRequired;

    public AddressValidator(boolean stateProvinceRequired) {
        this.stateProvinceRequired = stateProvinceRequired;
        logger.debug("AddressValidator created, this.stateProvinceRequired: " + this.stateProvinceRequired);
    }

    public boolean isStateProvinceRequired() {
        return stateProvinceRequired;
    }

    public List<ValidationCode> validateAddress(IAddress address, AbstractAddressValidationCodeResolver resolver) throws ValidationException {

        List<ValidationCode> errorCodes = new ArrayList<ValidationCode>();

        if (address != null) {

            TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            TextValidator shortDBCodeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
            TextValidator dBCodeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
            TextValidator spec50Validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SPEC_50_LENGTH);

            CodeValidationResult vr = shortDescValidator.validate(address.getAddress1());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.ADDRESS1),
                                    new StringArgument(address.getAddress1()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )

                    );
                }
            }

            vr = shortDescValidator.validate(address.getAddress2());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.ADDRESS2),
                                    new StringArgument(address.getAddress2()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )

                    );
                }
            }

            vr = shortDescValidator.validate(address.getAddress3());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.ADDRESS3),
                                    new StringArgument(address.getAddress3()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )

                    );
                }
            }

            vr = shortDescValidator.validate(address.getAddress4());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.ADDRESS4),
                                    new StringArgument(address.getAddress4()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )

                    );
                }
            }

            vr = shortDescValidator.validate(address.getCity());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.CITY),
                                    new StringArgument(address.getCity()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )
                    );
                }
            }
            
            if (isStateProvinceRequired()) {
                if (!Utility.isSet(address.getStateProvinceCd())) {
                    errorCodes.add(new ValidationCodeImpl(
                            ValidationReason.VALUE_IS_NOT_SET,
                            new StringArgument(IAddress.STATE_PROVINCE_CD),
                            new StringArgument(address.getStateProvinceCd())
                    ));
                } else {
                    vr = dBCodeValidator.validate(address.getStateProvinceCd());
                    if (vr != null && vr.getValidationCodes() != null) {
                        for (ValidationCode errorCode : vr.getValidationCodes()) {
                            errorCodes.add(
                                    new ValidationCodeImpl(
                                            errorCode.getReason(),
                                            new StringArgument(IAddress.STATE_PROVINCE_CD),
                                            new StringArgument(address.getStateProvinceCd()),
                                            new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH)
                                    )
                            );
                        }
                    }
                }
            } else {
                if (Utility.isSet(address.getStateProvinceCd())) {
                    errorCodes.add(new ValidationCodeImpl(
                            ValidationReason.VALUE_MUST_BE_EMPTY,
                            new StringArgument(IAddress.STATE_PROVINCE_CD),
                            new StringArgument(address.getStateProvinceCd())
                    ));
                }
            }

            vr = shortDescValidator.validate(address.getCountryCd());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.COUNTRY_CD),
                                    new StringArgument(address.getCountryCd()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH)
                            )

                    );
                }
            }

            vr = spec50Validator.validate(address.getCountyCd());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.COUNTY_CD),
                                    new StringArgument(address.getCountyCd()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SPEC_50_LENGTH)
                            )
                    );
                }
            }

            vr = shortDBCodeValidator.validate(address.getPostalCode());
            if (vr != null && vr.getValidationCodes() != null) {
                for (ValidationCode errorCode : vr.getValidationCodes()) {
                    errorCodes.add(
                            new ValidationCodeImpl(
                                    errorCode.getReason(),
                                    new StringArgument(IAddress.POSTAL_CODE),
                                    new StringArgument(address.getPostalCode()),
                                    new NumberArgument(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH)
                            )

                    );
                }
            }
        }

        logger.debug("validateAddress()=> errorCodes: " + errorCodes);

        return errorCodes;

    }

    @Override
    public CodeValidationResult validate(IAddress address, AbstractAddressValidationCodeResolver resolver) throws ValidationException {
        List<ValidationCode> errorCodes = validateAddress(address, resolver);
        return new CodeValidationResult(resolver, errorCodes.toArray(new ValidationCode[errorCodes.size()]));
    }

    @Override
    public CodeValidationResult validate(IAddress obj) throws ValidationException {
        return validate(obj, null);
    }
}
