package com.espendwise.manta.web.util;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.validation.ApplicationValidator;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.web.forms.ValidForm;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebAction {

    private static final Logger logger = Logger.getLogger(WebAction.class);

    public static enum ACTION {SAVE, CREATE, DELETE, ASSIGN}


    public static List<? extends ArgumentedMessage> validate(ValidForm form) throws Exception {

        List<ArgumentedMessage> messages = new ArrayList<ArgumentedMessage>();

        Class<Validation> annonClass = Utility.getAnnonClass(form.getClass(), Validation.class);

        if (annonClass != null) {

            Validation ann = annonClass.getAnnotation(Validation.class);

            if (ann.value() != null) {
                Class<? extends ApplicationValidator>[] validators = ann.value();
                for (Class<? extends ApplicationValidator> validator : validators) {
                    ApplicationValidator validatorImpl = validator.newInstance();
                    ValidationResult result = validatorImpl.validate(form);
                    if (result != null) {
                        messages.addAll(result.getResult());
                    }
                }
            }

        }

        return messages;

    }

    public static List<? extends ArgumentedMessage> validate(ValidForm form, ApplicationValidator... validators) {

        List<ArgumentedMessage> messages = new ArrayList<ArgumentedMessage>();

        if (validators != null) {

            for (ApplicationValidator validator : validators) {
                ValidationResult result = validator.validate(form);
                if (result != null) {
                    messages.addAll(result.getResult());
                }
            }

        }

        return messages;

    }


    public static void success(HttpServletRequest request, SuccessActionMessage message) {

        Map<MessageType, List<DisplayMessage>> messages;

        messages = WebTool.getWebMessages(request);

        List<DisplayMessage> confirmations = messages.get(MessageType.SUCCESS_MESSAGE);
        if (confirmations == null) {
            confirmations = new ArrayList<DisplayMessage>();
            messages.put(MessageType.SUCCESS_MESSAGE, confirmations);
        }

        confirmations.add(message);

    }

    public static void success(WebRequest request, SuccessActionMessage message, int scope) {

        logger.info("success()=> BEGIN, success web action, message:  " + message + ", scope: " + scope);

        Map<MessageType, List<DisplayMessage>> messages = WebTool.getWebMessages(request, scope);

        List<DisplayMessage> confirmations = messages.get(MessageType.SUCCESS_MESSAGE);
        if (confirmations == null) {
            confirmations = new ArrayList<DisplayMessage>();
            messages.put(MessageType.SUCCESS_MESSAGE, confirmations);
        }

        confirmations.add(message);

        logger.info("success()=> END, confirmations.size:  " + confirmations.size());
    }


    public static void success(WebRequest request, SuccessActionMessage message) {

        Map<MessageType, List<DisplayMessage>> messages;

        messages = WebTool.getWebMessages(request, WebRequest.SCOPE_REQUEST);

        List<DisplayMessage> confirmations = messages.get(MessageType.SUCCESS_MESSAGE);
        if (confirmations == null) {
            confirmations = new ArrayList<DisplayMessage>();
            messages.put(MessageType.SUCCESS_MESSAGE, confirmations);
        }

        confirmations.add(message);

    }

    public static void success(HttpSession session, SuccessActionMessage message) {

        Map<MessageType, List<DisplayMessage>> messages = WebTool.getWebMessages(session);

        List<DisplayMessage> confirmations = messages.get(MessageType.SUCCESS_MESSAGE);
        if (confirmations == null) {
            confirmations = new ArrayList<DisplayMessage>();
            messages.put(MessageType.SUCCESS_MESSAGE, confirmations);
        }

        confirmations.add(message);

    }

    public static void success(WebRequest request) {
        success(request, new SuccessActionMessage());
    }
    
    
    //Added this method to display error message 
    public static void error(HttpSession session, SuccessActionMessage message) {

    	Map<MessageType, List<DisplayMessage>> messages = WebTool.getWebMessages(session);

        List<DisplayMessage> confirmations = messages.get(MessageType.ERROR);
        if (confirmations == null) {
            confirmations = new ArrayList<DisplayMessage>();
            messages.put(MessageType.ERROR, confirmations);
        }

        confirmations.add(message);

    }

}
