package com.espendwise.manta.util.alert;


import com.espendwise.manta.util.arguments.TypedArgument;

public class ArgumentedMessageImpl extends  AbstractArgumentedMessage {

    public ArgumentedMessageImpl(String key, String defaultMessage) {
        super(key, defaultMessage);
    }

    public ArgumentedMessageImpl(String key, TypedArgument... arguments) {
        super(key, null, arguments);
    }

    public ArgumentedMessageImpl(String key) {
        super(key, null);
    }


}
