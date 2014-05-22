package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.arguments.TypedArgument;

public class ValidationCodeImpl extends ValidationCode {

    public ValidationCodeImpl(ValidationReason reason) {
        super(reason);
    }

    public ValidationCodeImpl(ValidationReason reason, TypedArgument... args) {
        super(reason, args);
    }
}
