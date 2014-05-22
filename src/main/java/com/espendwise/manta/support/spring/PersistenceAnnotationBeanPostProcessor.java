package com.espendwise.manta.support.spring;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

import java.beans.PropertyDescriptor;

public class PersistenceAnnotationBeanPostProcessor  extends org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor {

    private static final Logger logger = Logger.getLogger(PersistenceAnnotationBeanPostProcessor.class);

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        try {
            return super.postProcessPropertyValues(pvs, pds, bean, beanName);
        } catch (BeansException e) {
            logger.warn("WARNING: " + e.getMessage());
            return pvs;
        }
    }
}
