package com.espendwise.manta.util.alert;


import com.espendwise.manta.util.arguments.TypedArgument;

public class AbstractArgumentedMessage implements ArgumentedMessage {

    private String key;
    private String defaultMessage;
    private TypedArgument[] arguments;

    public AbstractArgumentedMessage(String key, String defaultMessage, TypedArgument... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public String getKey() {
        return key;
    }

    @Override
    public TypedArgument[] getArguments() {
        return arguments;
    }

    public String[] getCodes() {
        return new String[]{getKey()};
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
