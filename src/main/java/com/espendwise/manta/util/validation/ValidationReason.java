package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.trace.ApplicationReason;

public enum ValidationReason implements ApplicationReason {

    VALUE_IS_NOT_SET("0001", "The value is empty"),
    WRONG_NUMBER_FORMAT("0002", "Wrong number format"),
    INVALID_POSITIVE_VALUE("0003", "The value is negative , but the value must be >= 0"),
    WRONG_DATE_FORMAT("0004", "Wrong date format"),
    RANGE_OUT("0005", "The value range Out"),
    DATES_RANGE_OUT("0012", ""),
    VALUE_MUST_BE_EMPTY("0006", "The  value must be empty"),
    WRONG_EMAIL_ADDRESS_FORMAT("0007", "Wrong email address format"),
    RESULT_SIZE_LIMIT_OUT("0008", "Result size is out of the limit"),
    VALUE_IS_NOT_SUPPORTED("0009", "The value is not supported"),
    WRONG_DATE_FORMAT_EXT("0011", "Wrong date format"),
    WRONG_BOUNDS_RIGHT_BEFORE_LEFT("0012", "Right Bound is before Left Bound"),
    GENERIC_ERROR("0010", "Error");
    private String code;
    private String reason;

    ValidationReason(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ValidationReason{" +
                "code='" + code + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
