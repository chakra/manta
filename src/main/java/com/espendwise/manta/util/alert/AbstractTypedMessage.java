package com.espendwise.manta.util.alert;


import com.espendwise.manta.util.arguments.TypedArgument;

public class AbstractTypedMessage extends AbstractArgumentedMessage implements TypedMessage {

    private MessageType type;

    public AbstractTypedMessage(MessageType type, String key, String defaultMessage, TypedArgument... arguments) {
        super(key, defaultMessage, arguments);
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

}
