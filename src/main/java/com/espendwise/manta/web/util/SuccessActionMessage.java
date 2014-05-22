package com.espendwise.manta.web.util;


import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.TypedArgument;

public class SuccessActionMessage extends ActionMessage {

    public SuccessActionMessage() {
        super(MessageType.SUCCESS_MESSAGE, "admin.successMessage.success", null, null);
    }

    public SuccessActionMessage(String key, TypedArgument[] arguments) {
        super(MessageType.SUCCESS_MESSAGE, key, arguments, null);
    }

    public SuccessActionMessage(String key) {
        super(MessageType.SUCCESS_MESSAGE, key, null, null);
    }
}
