package com.espendwise.manta.web.util.smac;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Map;

public class SmacListener implements HttpSessionAttributeListener {

    private static final Logger logger = Logger.getLogger(SmacListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {

        logger.debug("attributeAdded()=> BEGIN");

        try {

            String attrName = httpSessionBindingEvent.getName();

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();


            logger.debug("attributeAdded()=> attrName: " + attrName + ", " + request.getRequestURI());

            ApplicationContext ctx =
                    WebApplicationContextUtils.
                            getWebApplicationContext(httpSessionBindingEvent.getSession().getServletContext());

            SmacHandler smacHandler = ctx.getBeansOfType(SmacHandler.class)
                    .values()
                    .iterator()
                    .next();

            logger.debug("attributeAdded()=> HandlerMappings: " + "{" + ctx.getBeansOfType(HandlerMapping.class) + "}");

            if (smacHandler.getDescriptions().containsKey(attrName)) {


                SmacDesc desc = smacHandler.getDescriptions().get(attrName);
                Map<String, String> pathParams = UrlPathAssistent.getPathVariables(request);
                String  processPath = UrlPathAssistent.createPath(desc.getHandlerPath(), pathParams);

                SmacMapping modelAttributesMapping = (SmacMapping) httpSessionBindingEvent.getSession().getAttribute(SmacMapping.SESSION_KEY);
                if (modelAttributesMapping == null) {
                    modelAttributesMapping = new SmacMapping();
                    httpSessionBindingEvent.getSession().setAttribute(SmacMapping.SESSION_KEY, modelAttributesMapping);
                }

                logger.debug("attributeAdded()=> bind path " + processPath);

                if (Utility.isSet(pathParams) && Utility.isSet(pathParams)) {

                    modelAttributesMapping.bindSma(
                            processPath,
                            new SmacDesc(desc.getHandlerPath(),
                                    desc.getName(),
                                    desc.getPathMapping(),
                                    desc.getController()
                            )
                    );
                }


            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        logger.debug("attributeAdded()=> END");

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

        logger.debug("attributeRemoved()=> BEGIN");
        logger.debug("attributeRemoved()=> remove value: " + httpSessionBindingEvent.getName());

        httpSessionBindingEvent.getSession().removeAttribute(httpSessionBindingEvent.getName());

        logger.debug("attributeRemoved()=> END");

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}
