package com.espendwise.tools.gencode.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProjectDbProperty {

    private final static Log logger = LogFactory.getLog(ProjectDbProperty.class);

    public static final String DATA_BASE_PREFIX = "database";
    public static final String DS_PREFIX = "ds";


    public static final String DATA_SOURCE_CLASS = "dataSourceClass";
    public static final String DATA_SOURCE_URL = "url";
    public static final String DATA_SOURCE_USER_NAME = "username";
    public static final String DATA_SOURCE_USER_PASSWORD = "password";

    public static final String MAIN_UNIT_ID = "dsmain";

    public static interface PROPERTY_TYPE {
        public static String DATASOURCE = "datasource";
        public static String HIBERNATE = "hibernate";
    }

    public static final String[] REQURED_DATA_SOURCE_FIELDS = new String[]{DATA_SOURCE_USER_PASSWORD
    };

    public static final String BASE_DS_CLASS = "org.apache.commons.dbcp.BasicDataSource";

    private String ubitId;
    private String type;
    private String propertyName;
    private String value;

    public ProjectDbProperty(String ubitId, String type,  String propertyName, String value) {
        this.ubitId = ubitId;
        this.type = type;
        this.propertyName = propertyName;
        this.value = value;
     }

    public String getUbitId() {
        return ubitId;
    }

    public void setUbitId(String ubitId) {
        this.ubitId = ubitId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String getPropertyName(String key, String type, String field) {
        return DATA_BASE_PREFIX + "." + key.toLowerCase() + "." + (type!=null?(type + "."):"") + field;
    }

    @Override
    public String toString() {
        return "ProjectDbProperty{" +
                "ubitId='" + ubitId + '\'' +
                ", type='" + type + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}