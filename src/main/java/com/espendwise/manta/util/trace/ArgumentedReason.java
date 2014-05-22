package com.espendwise.manta.util.trace;

import com.espendwise.manta.util.arguments.TypedArgument;

public interface ArgumentedReason<T extends ApplicationReason> {

    public TypedArgument[] getArguments();

    public T getReason();
}
