package com.espendwise.manta.web.util.smac;

import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.web.util.SessionKey;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class SessionalWbbMessageFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(SessionalWbbMessageFilter.class);


    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        logger.debug("doFilterInternal()=> BEGIN");

        try {

            Map<MessageType, List<DisplayMessage>> sessionMessages = (Map<MessageType, List<DisplayMessage>>) request.getSession().getAttribute(SessionKey.WEB_MESSAGES);

            logger.debug("doFilterInternal()=> sessionMessages: "+sessionMessages);

            if (sessionMessages != null && !sessionMessages.isEmpty()) {
    
                Map<MessageType, List<DisplayMessage>> reqAttributes = (Map<MessageType, List<DisplayMessage>>) request.getAttribute(SessionKey.WEB_MESSAGES);
                if (reqAttributes == null) {
                    reqAttributes = new HashMap<MessageType, List<DisplayMessage>>();
                    request.setAttribute(SessionKey.WEB_MESSAGES, reqAttributes);
                }
    
                for (Map.Entry<MessageType, List<DisplayMessage>> m : sessionMessages.entrySet()) {
                    List<DisplayMessage> value = m.getValue();
    
                    List<DisplayMessage> l = reqAttributes.get(m.getKey());
                    if (l == null) {
                        l = new ArrayList<DisplayMessage>();
                        reqAttributes.put(m.getKey(), l);
                    }
    
                    if (value != null) {
                        Iterator<DisplayMessage> it = value.iterator();
                        while (it.hasNext()) {
                            l.add(it.next());
                            it.remove();
                        }
                    }
    
                }
    
                request.getSession().removeAttribute(SessionKey.WEB_MESSAGES);

                logger.info("doFilterInternal()=> session messages has been processed and removed from session.");

            }

        } catch (Exception e) {

            logger.error("doFilterInternal()=> ERROR: " + e.getMessage(), e);
       
        }

        filterChain.doFilter(request, response);

        logger.debug("doFilterInternal()=> END.");

    }

}
