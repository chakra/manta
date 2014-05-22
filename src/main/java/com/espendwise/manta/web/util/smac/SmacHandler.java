package com.espendwise.manta.web.util.smac;


import com.espendwise.manta.spi.AutoClean;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class SmacHandler implements ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(SmacHandler.class);

    private Map<String, SmacDesc> descriptions;

    private ApplicationContext applicationContext;

    public SmacHandler() {
        this.descriptions = new HashMap<String, SmacDesc>();
    }

    public void init() {

        Map<String, Class> controllers = getBeansWithAnnotation(Controller.class);

        for (Class controller : controllers.values()) {

            AutoClean sessionStrategy = (AutoClean) controller.getAnnotation(AutoClean.class);

            if (sessionStrategy != null) {

                Class[] cleanControllers = sessionStrategy.controller() == null || sessionStrategy.controller().length == 0
                        ? new Class[]{controller}
                        : sessionStrategy.controller();

                for(Class cleanController:cleanControllers) {

                    SessionAttributes sessionAttributes = (SessionAttributes) cleanController.getAnnotation(SessionAttributes.class);
                    RequestMapping requestMapping = (RequestMapping) cleanController.getAnnotation(RequestMapping.class);

                    Set<String> autoCleanFor = sessionStrategy.value() != null
                            ? new HashSet<String>(Arrays.asList(sessionStrategy.value()))
                            : null;


                    String[] attributes = sessionStrategy.value() != null && sessionStrategy.value().length > 0
                            ? sessionStrategy.value()
                            : sessionAttributes != null
                            ? sessionAttributes.value()
                            : null;

                    if (attributes != null && attributes.length > 0) {

                        String[] pathList = requestMapping != null
                                ? requestMapping.value()
                                : null;

                        if (pathList != null) {

                            for (String path : pathList) {

                                for (String attr : attributes) {

                                    if (autoCleanFor == null || autoCleanFor.contains(attr)) {

                                        Set<String> pathSet = new HashSet<String>();

                                        for (Method m : cleanController.getDeclaredMethods()) {
                                            RequestMapping methodMappingPath = m.getAnnotation(RequestMapping.class);
                                            if (methodMappingPath != null) {
                                                for (String mp : methodMappingPath.value()) {
                                                    pathSet.add(path + mp);
                                                }
                                            }
                                        }

                                        SmacDesc desc = new SmacDesc(path, attr, pathSet, cleanController.getSimpleName().toUpperCase());

                                        logger.info("init()=> controller: +"+cleanController+", desc: "+desc.getName());
                                        descriptions.put(attr, desc);
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
    }


    public Map<String, SmacDesc> getDescriptions() {
        return descriptions;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, Class> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Class> results = new HashMap<String, Class>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class beanType = applicationContext.getType(beanName);
            if (beanType.getAnnotation(annotationType) != null) {
                results.put(beanName, beanType);
            }
        }
        return results;
    }
}
