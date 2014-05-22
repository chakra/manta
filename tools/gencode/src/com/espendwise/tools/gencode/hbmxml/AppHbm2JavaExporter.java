package com.espendwise.tools.gencode.hbmxml;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2x.POJOExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;


public class AppHbm2JavaExporter extends POJOExporter {

    private static final String T = "T";
    private static final String Y = "Y";
    private static final String T_INT = "1";

    private static final String POJO_CLASS_ALL = "all";
    private static final String POJO_CLASS_NOT_SET = "${javaname}";

    public static final String CLASS_VERSION_UID_PROPERTIES = "classVersionUID";
    public static final String POJO_CLASS = "javaname";
    public static final String USE_CLASS_VERSION_UID_PROPERTIES = "useClassVesionUID";

    private Properties versionUIDProperties;
    protected Set<String> javaname;

    public AppHbm2JavaExporter() {
        super();
  }

    public void init() {
        super.init();
        versionUIDProperties = new Properties();
        javaname = new HashSet<String>();
    }

    protected void setupContext() {
        super.setupContext();
        initSerialVersionUIDProperties();
        initPOJOClasses();
    }

    protected void exportPOJO(Map pMap, POJOClass pPojoClass) {
        if (javaname.isEmpty() || javaname.contains(pPojoClass.getShortName())) {
            super.exportPOJO(pMap,
                    new AppEntityPOJOClass(
                            getMappedClass(pPojoClass),
                            getConfiguration(),
                            getCfg2JavaTool(),
                            getSerialVersionUID(pPojoClass)
                    )
            );
        }
    }

    protected String getSerialVersionUID(POJOClass pPojoClass) {
        if (isTrue(getProperties().getProperty(USE_CLASS_VERSION_UID_PROPERTIES))) {
            PersistentClass mappedClass = getMappedClass(pPojoClass);
            String versionUID = versionUIDProperties.getProperty(mappedClass.getClassName());
            if (versionUID != null) {
                return versionUID;
            } else {
                return "-1";
            }
        } else {
            return null;
        }


    }

    protected PersistentClass getMappedClass(POJOClass pojoClass) {
        return getConfiguration().getClassMapping(pojoClass.getPackageName() + "." + pojoClass.getShortName());
    }

    private void initSerialVersionUIDProperties() {

        Properties properties = getProperties();

        String serialVerUIDPropertiesFile = properties.getProperty(CLASS_VERSION_UID_PROPERTIES);
        if (serialVerUIDPropertiesFile != null && serialVerUIDPropertiesFile.length() > 0) {
            File file = new File(serialVerUIDPropertiesFile);
            if (file.exists()) {
                try {
                    FileInputStream inputStream = new FileInputStream(serialVerUIDPropertiesFile);
                    versionUIDProperties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean isTrue(String pValue) {

        if (pValue != null) {
            pValue = pValue.trim();
        }

        if (pValue != null && pValue.length() > 0) {
            char first = pValue.substring(0, 1).toCharArray()[0];
            if (Character.isDigit(first)) {
                return Integer.parseInt(pValue) > 0;
            } else {
                return (String.valueOf(first).equalsIgnoreCase(T)
                        || String.valueOf(first).equalsIgnoreCase(Y)
                        || String.valueOf(first).equalsIgnoreCase(T_INT));
            }
        }

        return false;
    }

    private void initPOJOClasses() {

        Properties properties = getProperties();
        String javanameProperty = properties.getProperty(POJO_CLASS);
        if (javanameProperty != null && javanameProperty.length() > 0) {
            for (String s : javanameProperty.split(",")) {
                if (s != null && s.trim().length() > 0)
                    javaname.add(s);
            }
        }

        javaname.remove(POJO_CLASS_ALL);
        javaname.remove(POJO_CLASS_NOT_SET);

    }

    public void exportSuperPOJO(Map pMap, POJOClass pPOJOClass) {
        if (javaname.isEmpty() || javaname.contains(pPOJOClass.getShortName())) {
            super.exportPOJO(pMap, pPOJOClass);
        }
    }

}


