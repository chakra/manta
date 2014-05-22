package com.espendwise.manta.util.trace;


import com.espendwise.manta.util.arguments.TypedArgument;

import java.util.Arrays;

public class ApplicationExceptionCode<T extends ApplicationReason> implements ArgumentedReason {

    private T  reason;
    private TypedArgument[] args;

    public ApplicationExceptionCode(T reason) {
        this.reason = reason;
    }

    public ApplicationExceptionCode(T reason, TypedArgument... args) {
        this.reason = reason;
        this.args = args;
    }

    @Override
    public TypedArgument[] getArguments() {
        return this.args;
    }

    @Override
    public T getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ApplicationExceptionCode{" +
                "reason=" + reason +
                ", args=" + (args == null ? null : Arrays.asList(args)) +
                '}';
    }
}
