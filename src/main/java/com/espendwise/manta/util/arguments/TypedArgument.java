package com.espendwise.manta.util.arguments;


import com.espendwise.manta.spi.Typed;
import com.espendwise.manta.util.arguments.Argument;
import com.espendwise.manta.util.arguments.ArgumentType;

public interface TypedArgument<T> extends Argument<T>, Typed<ArgumentType> {

}
