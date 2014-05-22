package com.espendwise.manta.web.util;

import com.espendwise.manta.spi.Resolver;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.manta.util.trace.ServiceLayerException;
import com.espendwise.manta.util.validation.ValidationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebErrors extends ActionMessages {

    public WebErrors() {
    }

    public WebErrors(WebRequest request) {
        super();
        messages = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES, WebRequest.SCOPE_REQUEST);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            request.setAttribute(SessionKey.WEB_MESSAGES, messages, WebRequest.SCOPE_REQUEST);
        }
    }
    
    public WebErrors(WebRequest request, int scope) {
        super();
        messages = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES, scope);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            request.setAttribute(SessionKey.WEB_MESSAGES, messages, WebRequest.SCOPE_REQUEST);
        }
    }

    public WebErrors(HttpServletRequest request) {
        super();
        messages = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            request.setAttribute(SessionKey.WEB_MESSAGES, messages);
        }
    }

    public void putError(String key, TypedArgument... arguments) {
        add(new WebError(key, arguments));
    }

    public void putError(DisplayMessage message) {
        add(MessageType.ERROR, message);
    }

    public void putWarning(DisplayMessage message) {
        add(MessageType.WARNING, message);
    }

    public void putMessage(DisplayMessage message) {
        super.add(MessageType.MESSAGE, message);
    }

    public void putMessage(String key, TypedArgument... arguments) {
        add(new WebError(key, arguments));
    }

    public void putErrors(List<? extends MessageSourceResolvable> errors) {
        if (Utility.isSet(errors)) {
            for (MessageSourceResolvable err : errors) {
                putError(new WebError(err.getCodes()[0], Args.typed(err.getArguments()), err.getDefaultMessage()));
            }
        }
    }

    public void putErrors(ValidationException e, ValidationResolver<ValidationException> resolver) {
        List<? extends ArgumentedMessage> errors = resolver.resolve(e);
        if (Utility.isSet(errors)) {
            for (MessageSourceResolvable err : errors) {
                putError(new WebError(err.getCodes()[0], Args.typed(err.getArguments()), err.getDefaultMessage()));
            }
        }
    }

    public <T extends ApplicationRuntimeException> void putErrors(T e, Resolver<T, List<ArgumentedMessage>> resolver) {
        List<? extends ArgumentedMessage> errors = resolver.resolve(e);
        if (Utility.isSet(errors)) {
            for (MessageSourceResolvable err : errors) {
                putError(new WebError(err.getCodes()[0], Args.typed(err.getArguments()), err.getDefaultMessage()));
            }
        }
    }


    public <T extends ServiceLayerException> void putErrors(T e, Resolver<T, List<ArgumentedMessage>> resolver) {
        List<? extends ArgumentedMessage> errors = resolver.resolve(e);
        if (Utility.isSet(errors)) {
            for (MessageSourceResolvable err : errors) {
                putError(new WebError(err.getCodes()[0], Args.typed(err.getArguments()), err.getDefaultMessage()));
            }
        }
    }
}
