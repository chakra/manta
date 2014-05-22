package com.espendwise.manta.web.tags;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.web.util.SessionKey;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WebMessagesTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(WebMessagesTag.class);

    public MessageType type;
    public String var;

    private List<DisplayMessage> displayMessages;
    private AppLocale appLocale;

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public AppLocale getAppLocale() {
        return appLocale;
    }

    public List<DisplayMessage> getDisplayMessages() {
        return displayMessages;
    }

    @Override
    public int doStartTag() throws JspException {

        logger.debug("doStartTag()=> BEGIN");

        this.displayMessages = getWebMessages(pageContext, getType());
        this.appLocale = new AppLocale(Auth.getAppUser().getLocale());

        logger.debug("doStartTag() found  " + displayMessages.size() + "  messages");

        logger.debug("doStartTag()=> END, OK!");

        return TagSupport.EVAL_PAGE;
    }


    public static List<DisplayMessage> getWebMessages(PageContext pageContext, MessageType type) {

        logger.debug("getWebMessages()=> BEGIN, type: " + type);

        List<DisplayMessage> displayMessages = new ArrayList<DisplayMessage>();

        Map<MessageType, List<DisplayMessage>> webMessages;

        webMessages = (Map<MessageType, List<DisplayMessage>>) pageContext.findAttribute(SessionKey.WEB_MESSAGES);
        if (webMessages != null && !webMessages.isEmpty()) {
            List<DisplayMessage> messages = webMessages.get(type);
            if (Utility.isSet(messages)) {
                displayMessages.addAll(messages);
            }
        }

        logger.debug("getWebMessages()=> END.");

        return displayMessages;
    }

    @Override
    public int doEndTag() throws JspException {

        List<String> resolverMessages = new ArrayList<String>();

        AppLocale locale = getAppLocale();

        List<DisplayMessage> messages = getDisplayMessages();
        if (messages != null && !messages.isEmpty()) {
            for (DisplayMessage message : messages) {
                resolverMessages.add(message.resolve(locale));
            }
        }

        pageContext.setAttribute(getVar(), resolverMessages);

        return TagSupport.EVAL_PAGE;
    }

    @Override
    public void release() {
        this.displayMessages = null;
        this.appLocale = null;
        this.type= null;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
