package com.espendwise.manta.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;

import javax.annotation.Resource;
import java.util.*;

@Resource(mappedName = ResourceNames.APPLICATION_SETTINGS)
public class ApplicationSettings {

    private static Map<String, Map<String, String>> settings = new HashMap<String, Map<String, String>>();

    private static Log logger = LogFactory.getLog(ApplicationSettings.class);


    private Properties properties;
    private Properties defaultProperties;

    private String placeHolderPrefix = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX;
    private String placeHolderSuffix = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX;


    private ApplicationSettings() {
    }


    public synchronized void init() {

        logger.info("init()=> BEGIN");

        try {

            Map<String, String> properties = new HashMap<String, String>();
            Set<String> emptyProperties = new HashSet<String>();

            for (Map.Entry<Object, Object> e : this.properties.entrySet()) {
                properties.put((String) e.getKey(), (String) e.getValue());
            }

            Enumeration<?> settingsPropertyKeys = defaultProperties.propertyNames();

            while (settingsPropertyKeys.hasMoreElements()) {

                String key = (String) settingsPropertyKeys.nextElement();
                String value = (String) defaultProperties.get(key);

                if (value != null) {
                    if (value.startsWith(placeHolderPrefix) && value.endsWith(placeHolderSuffix)) {
                        value = (String) this.properties.get(value.substring(2, value.length() - 1));
                        if (value == null) {
                            logger.warn("init()=> [override] Property with key [" + key + "] is empty");
                            emptyProperties.add(key);
                        }
                        logger.info("init()=> [override] override ['" + key + "'] -> " + value);
                    }
                }

                properties.put(key, value);

            }

            if (!emptyProperties.isEmpty()) {
                  throw new Exception("Check your configuration file, " +
                          "properties "+emptyProperties+" must be defined prior to use");
            }

            logger.info("init()=> properties.size: " + properties.size());

            if (!properties.isEmpty()) {
                settings.put(null, properties);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Failed to initialize application properties. " + e.getMessage(), e);
        }


        logger.info("init()=> settings.size: " + settings.size());
        logger.info("init()=> END.");

    }


    public String getSettings(String property) {
        return getSettings(null, property);
    }

    public String getSettings(String key, String property) {

        String value = null;

        Map<String, String> properties = settings.get(key);
        if (properties != null && !properties.isEmpty()) {
            value = properties.get(property);
            if (value != null) {
                return value;
            }
        }

        if (key == null) {
            return value;
        }

        properties = settings.get(null);
        if (properties != null && !properties.isEmpty()) {
            value = properties.get(property);
            if (value != null) {
                return value;
            }
        }

        return value;

    }


    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setDefaultProperties(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public Properties getDefaultProperties() {
        return defaultProperties;
    }

    public String getPlaceHolderSuffix() {
        return placeHolderSuffix;
    }

    public void setPlaceHolderSuffix(String placeHolderSuffix) {
        this.placeHolderSuffix = placeHolderSuffix;
    }

    public String getPlaceHolderPrefix() {
        return placeHolderPrefix;
    }

    public void setPlaceHolderPrefix(String placeHolderPrefix) {
        this.placeHolderPrefix = placeHolderPrefix;
    }
}
