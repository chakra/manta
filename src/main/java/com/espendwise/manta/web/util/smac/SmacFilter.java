package com.espendwise.manta.web.util.smac;


import com.espendwise.manta.auth.ApplicationContextSecurityFilter;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.Hierarchy;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmacFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(SmacFilter.class);

    public SmacFilter() {
    }


    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        logger.debug("doFilterInternal()=> BEGIN");


        try {

            doFilterInternal(request);

        } catch (Exception e) {
            //ignore
        }

        filterChain.doFilter(request, response);

        logger.debug("doFilterInternal()=> END");

    }


    private void doFilterInternal(HttpServletRequest request) {

        Map<String, Object> context = (Map<String, Object>) request.getAttribute(ApplicationContextSecurityFilter.REQUEST_CONTEXT);

        SmacMapping modelAttributesMapping = (SmacMapping) request.getSession().getAttribute(SmacMapping.SESSION_KEY);

        if (context != null && modelAttributesMapping != null) {

            String handlerPath = (String) context.get(ApplicationContextSecurityFilter.REQUEST_CONTEXT_ATTRIBUTE.HANDLER_PATH);
            HandlerMethod handler = (HandlerMethod) context.get(ApplicationContextSecurityFilter.REQUEST_CONTEXT_ATTRIBUTE.HANDLER);


            List<Map<String, SmacDesc>> toCleaning = new ArrayList<Map<String, SmacDesc>>();

            String[] pathParts = handlerPath.split("/");

            if (pathParts != null && pathParts.length > 0 && !isLocate(handler)) {

                Hierarchy<String, Map<String, SmacDesc>> mappings = modelAttributesMapping.getMappings();

                int i = 0;
                for (String pathPart : pathParts) {

                    i++;

                    if (mappings != null) {

                        Iterator<Hierarchy<String, Map<String, SmacDesc>>> it = mappings.values().iterator();

                        while (it.hasNext()) {

                            Hierarchy<String, Map<String, SmacDesc>> maPathMapping = it.next();

                            if (!maPathMapping.getKey().equalsIgnoreCase(pathPart)) {
                                toCleaning.addAll(maPathMapping.getAllValues());
                                it.remove();
                            } else {
                                mappings = maPathMapping;
                            }
                        }

                    } else {

                        break;

                    }
                }
            }


            Iterator<Map<String, SmacDesc>> it = toCleaning.iterator();
            while (it.hasNext()) {
                Map<String, SmacDesc> value = it.next();
                for(SmacDesc desc: value.values())
                request.getSession().removeAttribute(desc.getName());
                it.remove();
            }

        }

    }

    private boolean isLocate(HandlerMethod handler) {
        return (handler != null && handler.getBean().getClass().isAnnotationPresent(Locate.class));
    }

}
