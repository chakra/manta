package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class ValidationCode extends ApplicationExceptionCode<ValidationReason> {

    public ValidationCode(ValidationReason reason) {
        super(reason);
    }

    public ValidationCode(ValidationReason reason, TypedArgument... args) {
        super(reason, args);
    }


}
