package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.Constants;

import java.util.Date;
import java.util.Set;

public class Validators {

    public static IntegerValidator getIntegerValidator() {
        return new IntegerValidator();
    }
    
    public static LongValidator getLongValidator() {
        return new LongValidator();
    }
    
    public static DoubleValidator getDoubleValidator() {
        return new DoubleValidator();
    }
 
    public static IdValidator getIdValidator() {
        return new IdValidator();
    }

    public static DateValidator getDateValidator(String pattern) {
        return new DateValidator(pattern,
                Constants.VALIDATION_FIELD_CRITERIA.MIN_DATE.getTime(),
                Constants.VALIDATION_FIELD_CRITERIA.MAX_DATE.getTime()
        );
    }

    public static DateValidator getDateValidator(String pattern, Date leftBound, Date rightBound) {
        return new DateValidator(pattern, leftBound, rightBound);
    }

    public static TextValidator getTextValidator(int maxSize) {
        return getTextValidator(maxSize, true);
    }

    public static TextValidator getTextValidator(int maxSize, boolean isRequired) {
        return new TextValidator(maxSize, isRequired);
    }

    public static TextValidator getTextValidator(int maxSize, boolean isRequired, Set<String> supportedValues) {
        return new TextValidator(maxSize, isRequired, supportedValues);
    }

    public static AddressValidator getAddressValidator(boolean stateProvinceRequired) {
        return new AddressValidator(stateProvinceRequired);
    }

    public static ContactValidator getContactValidator(boolean stateProvinceRequired) {
        return new ContactValidator(stateProvinceRequired);
    }
    
    public static UserContactValidator getUserContactValidator(boolean stateProvinceRequired) {
        return new UserContactValidator(stateProvinceRequired);
    }

    public static AmountValidator getAmountValidator() {
        return new AmountValidator();
    }
    
    public static AmountValidator getAmountValidator(Integer precision, Integer scale) {
        return new AmountValidator(precision, scale);
    }

    public static AmountEmptyAllowedValidator getAmountEmptyAllowedValidator() {
        return new AmountEmptyAllowedValidator();
    }
    
    public static AmountEmptyAllowedValidator getAmountEmptyAllowedValidator(Integer precision, Integer scale) {
        return new AmountEmptyAllowedValidator(precision, scale);
    }
    
    public static PercentIntValidator getPercentIntValidator() {
        return new PercentIntValidator();
    }
    
    public static EmailAddressValidator getEmailAddressValidator() {
        return getEmailAddressValidator(false);
    }
    public static FilterResultValidator getFilterResultValidator(Integer limit ) {
        return new FilterResultValidator(limit);
    }   
    public static EmailAddressValidator getEmailAddressValidator(boolean allowMultipleAddresses) {
        return new EmailAddressValidator(allowMultipleAddresses);
    }

}
