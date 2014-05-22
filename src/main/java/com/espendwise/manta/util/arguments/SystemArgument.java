package com.espendwise.manta.util.arguments;

public class SystemArgument<T> extends AbstractArgument<T> {

    public SystemArgument(T c) {
        super(c, null, null, null);
    }

    @Override
    public String resolve() {
        return get().toString();
    }
}
