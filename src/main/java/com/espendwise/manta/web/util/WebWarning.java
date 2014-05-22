package com.espendwise.manta.web.util;

import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.TypedArgument;

public class WebWarning extends ActionMessage {

    public WebWarning(String key, TypedArgument[] arguments, String defaultMessage) {
        super(MessageType.WARNING, key, arguments, defaultMessage);
    }

    public WebWarning(String key, TypedArgument[] arguments) {
        super(MessageType.WARNING, key, arguments, null);
    }

    public WebWarning(String key) {
        super(MessageType.WARNING, key, null, null);
    }
}