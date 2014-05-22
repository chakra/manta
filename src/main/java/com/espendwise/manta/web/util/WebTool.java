package com.espendwise.manta.web.util;


import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.ApplicationSettings;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.IOUtility;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebTool {

    private static final Logger logger = Logger.getLogger(WebTool.class);

    public static Map<MessageType, List<DisplayMessage>> getWebMessages(HttpServletRequest request) {
        Map<MessageType, List<DisplayMessage>> messages = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            request.setAttribute(SessionKey.WEB_MESSAGES, messages);
        }
        return  messages;
    }

    public static Map<MessageType, List<DisplayMessage>> getWebMessages(HttpSession session) {
        Map<MessageType, List<DisplayMessage>> messages = (Map<MessageType, List<DisplayMessage>>) session.getAttribute(SessionKey.WEB_MESSAGES);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            session.setAttribute(SessionKey.WEB_MESSAGES, messages);
        }
        return messages;
    }

    public static Map<MessageType, List<DisplayMessage>> getWebMessages(WebRequest request, int scope) {

        Map<MessageType, List<DisplayMessage>> messages = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES, scope);
        if (messages == null) {
            messages = new HashMap<MessageType, List<DisplayMessage>>();
            request.setAttribute(SessionKey.WEB_MESSAGES, messages, scope);
        }

        return messages;
    }

    public static byte[] getDefaultLogo(HttpServletRequest request) {

        logger.debug("getDefaultLogo()=> BEGIN");

        byte[] logo = new byte[0];

        ApplicationSettings settings= AppResourceHolder.getAppResource().getApplicationSettings();

        String path = settings.getSettings(null, Constants.APPLICATION_SETTINGS.CONTENT_DEFAULT_PATH);
        path = path  + Constants.DEFAULT_IMAGE_PATH +Constants.DEFAULT_LOGO_PATH;

        ServletContextResource resource = new ServletContextResource(RequestContextUtils
                .getWebApplicationContext(request)
                .getServletContext(),
                path
        );

        if (resource.exists()) {
            try {
                logo = IOUtility.toBytes(resource.getInputStream());
            } catch (IOException e) {
                logger.info("getDefaultLogo()=>  error: "+e.getMessage());
            }
        }


        logger.debug("getDefaultLogo()=> END.");
        return logo == null ? new byte[0] : logo;

    }

}
