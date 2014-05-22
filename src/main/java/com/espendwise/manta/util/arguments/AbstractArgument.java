package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.alert.DisplayOutput;

public abstract class AbstractArgument<T> implements TypedArgument<T>, DisplayOutput<T> {

    private T c;
    private String defaultValue;
    protected ArgumentResolver<T> resolver;
    private ArgumentType argumentType;


    public AbstractArgument(T c, String defaultValue, ArgumentResolver<T> resolver, ArgumentType argumentType) {
        this.c = c;
        this.resolver = resolver;
        this.argumentType = argumentType;
        this.defaultValue = defaultValue;
    }
    public AbstractArgument(T c, ArgumentResolver<T> resolver, ArgumentType argumentType) {
        this.c = c;
        this.resolver = resolver;
        this.argumentType = argumentType;
        this.defaultValue = null;
    }

    public T get() {
        return c;
    }

    public String resolve() {
        String value = resolver.resolve(get());
        return value == null ? getDefaultValue() : value;
    }

    public ArgumentType getType() {
        return argumentType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "AbstractArgument{" +
                "c=" + c +
                ", defaultValue='" + defaultValue + '\'' +
                ", resolver=" + resolver +
                ", argumentType=" + argumentType +
                '}';
    }




}
