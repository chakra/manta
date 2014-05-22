package com.espendwise.manta.web.tags;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.util.Escaper;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.web.util.WebError;

public class WebErrorsTag extends RequestContextAwareTag {

    private static final Logger logger = Logger.getLogger(WebErrorsTag.class);

    public String includeFormErrors;

    private List<DisplayMessage> displayMessages;
    private AppLocale appLocale;

    public String getIncludeFormErrors() {
        return includeFormErrors;
    }

    public void setIncludeFormErrors(String includeFormErrors) {
        this.includeFormErrors = includeFormErrors;
    }

    public boolean includeFormErrors(){
        return Utility.isTrue(includeFormErrors);
    }

    public AppLocale getAppLocale() {
        return appLocale;
    }

    public List<DisplayMessage> getDisplayMessages() {
        return displayMessages;
    }

    @Override
    protected int doStartTagInternal() throws Exception {

        logger.debug("doStartTagInternal()=> BEGIN");

        this.displayMessages = new ArrayList<DisplayMessage>();
        this.appLocale = new AppLocale(Auth.getAppUser().getLocale());

        provideWebErrors();
        if (includeFormErrors()) {
            provideFormErrors();
        }

        logger.debug("doStartTagInternal() found: " + displayMessages.size() + " display errors");


        logger.debug("doStartTagInternal()=> END, OK!");

        return TagSupport.EVAL_PAGE;
    }


    private void provideWebErrors() {
        List<DisplayMessage> errors = WebMessagesTag.getWebMessages(pageContext, MessageType.ERROR);
        putToDisplay(errors.toArray(new DisplayMessage[errors.size()]));
    }

    public void provideFormErrors() {

        RequestContext requestContext = getRequestContext();

        for (String modelName : getFormFormNames(requestContext)) {

            logger.debug("provideFormErrors()=> provide model " + modelName);

            BindStatus status = requestContext.getBindStatus(modelName + ".*");

            logger.debug("provideFormErrors()=> " + status);

            if (status != null) {
                Errors errors = status.getErrors();
                logger.debug("provideFormErrors()=> errors " + errors);
                if (errors != null) {
                    putToDisplay(retriveWebErrors(errors));
                }
            }
        }

    }

    private List<String> getFormFormNames(RequestContext requestContext) {

        logger.debug("getFormFormNames()=> BEGIN");

        List<String> modelNames = new ArrayList<String>();

        Enumeration<String> attrs = pageContext.getRequest().getAttributeNames();
        if (attrs != null && attrs.hasMoreElements()) {
            while (attrs.hasMoreElements()) {
                String attrName = attrs.nextElement();
                if (attrName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
                    modelNames.add(attrName.substring(BindingResult.MODEL_KEY_PREFIX.length()));
                }
            }
        }

        logger.debug("getFormFormNames()=> END, founds: " + modelNames);

        return modelNames;
    }

    private void putToDisplay(DisplayMessage... displayMessages) {
        if (displayMessages != null && displayMessages.length > 0) {
            this.displayMessages.addAll(Arrays.asList(displayMessages));
        }
    }

    private DisplayMessage[] retriveWebErrors(Errors errors) {

        DisplayMessage[] webErrorArray = null;

        List<ObjectError> objectErrors = errors.getAllErrors();
        if (objectErrors != null) {

            webErrorArray = new DisplayMessage[objectErrors.size()];
            int i = 0;
            for (ObjectError objectError : objectErrors) {
                webErrorArray[i++] = new WebError(objectError);
            }
        }

        return webErrorArray;
    }

    @Override
    public int doEndTag() throws JspException {

        StringBuilder sb = new StringBuilder();

        AppLocale locale = getAppLocale();
        List<DisplayMessage> messages = getDisplayMessages();

        if (messages != null && !messages.isEmpty()) {

            for (DisplayMessage message : messages) {
                String resolvedMessage = message.resolve(locale);
                String escapedResolvedMessage = Escaper.htmlEscape(resolvedMessage);
                sb.append("<div class=\"webError\">");
                sb.append(escapedResolvedMessage);
                sb.append("</div>");
            }

            try {

                pageContext.getOut().write(sb.toString());

            } catch (IOException e) {   // ignore

            }

        }

        return TagSupport.EVAL_PAGE;
    }

    @Override
    public void doFinally() {
        super.doFinally();
        displayMessages = null;
        appLocale = null;
        includeFormErrors = null;
    }


}