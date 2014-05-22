package com.espendwise.manta.web.util;

import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.TypedArgument;

public class WebMessage extends ActionMessage {

    public WebMessage(String key, TypedArgument[] arguments, String defaultMessage) {
        super(MessageType.MESSAGE, key, arguments, defaultMessage);
    }

    public WebMessage(String key, TypedArgument[] arguments) {
        super(MessageType.MESSAGE, key, arguments, null);
    }

    public WebMessage(String key) {
        super(MessageType.MESSAGE, key, null, null);
    }


}