package com.espendwise.manta.web.util;

import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.TypedArgument;
import org.springframework.context.MessageSourceResolvable;


public class WebError extends ActionMessage {

    public WebError(String key, TypedArgument[] arguments, String defaultMessage) {
        super(MessageType.ERROR, key, arguments, defaultMessage);
    }

    public WebError(String key, TypedArgument... arguments) {
        super(MessageType.ERROR, key, arguments, null);
    }

    public WebError(ArgumentedMessage err) {
       super(MessageType.ERROR, err.getKey(), err.getArguments(), err.getDefaultMessage());
    }

    public WebError(MessageSourceResolvable err) {
        super(MessageType.ERROR, err.getCodes() != null ? err.getCodes()[0] : null, Args.typed(err.getArguments()), err.getDefaultMessage());
    }
}
