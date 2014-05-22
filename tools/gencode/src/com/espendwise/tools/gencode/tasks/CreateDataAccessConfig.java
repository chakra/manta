package com.espendwise.tools.gencode.tasks;

import com.espendwise.tools.gencode.hbmxml.persistence.HbmPersistenceXmlAssist;
import com.espendwise.tools.gencode.hbmxml.persistence.Persistence;
import com.espendwise.tools.gencode.spring.dbaccessxml.SpringDatabaseAccessXmlAssist;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.Beans;
import com.espendwise.tools.gencode.util.DataAccessJavaClassGenerator;
import com.espendwise.tools.gencode.util.ProjectDbProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

public class CreateDataAccessConfig extends Task {

    private final static Log logger = LogFactory.getLog(CreateDataAccessConfig.class);

    public String propertyFile;
    public String outDataAccessXmlFile;
    public String outPersistenceXmlFile;
    public String outDbProperiesFile;
    public String outDataAccessJavaFile;
    private String datasourcesCount;

    private static final Integer DEFAULT_DS_COUNT = 30;

    private static final String SPRING_DATA_ACCESS_SCHEMA_LOCATION = "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" +
            " http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd" +
            " http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd" +
            " http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd";


    public String getOutDataAccessJavaFile() {
        return outDataAccessJavaFile;
    }

    public void setOutDataAccessJavaFile(String outDataAccessJavaFile) {
        this.outDataAccessJavaFile = outDataAccessJavaFile;
    }

    public String getOutDataAccessXmlFile() {
        return outDataAccessXmlFile;
    }

    public void setOutDataAccessXmlFile(String outDataAccessXmlFile) {
        this.outDataAccessXmlFile = outDataAccessXmlFile;
    }

    public String getOutPersistenceXmlFile() {
        return outPersistenceXmlFile;
    }

    public void setOutPersistenceXmlFile(String outPersistenceXmlFile) {
        this.outPersistenceXmlFile = outPersistenceXmlFile;
    }

    public String getOutDbProperiesFile() {
        return outDbProperiesFile;
    }

    public void setOutDbProperiesFile(String outDbProperiesFile) {
        this.outDbProperiesFile = outDbProperiesFile;
    }

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String getDatasourcesCount() {
        return datasourcesCount;
    }

    public void setDatasourcesCount(String datasourcesCount) {
        this.datasourcesCount = datasourcesCount;
    }

    public void execute() throws BuildException {

        logger.info("#########################################################");
        logger.info("# [CreateDataAccessConfig] PROPERTIES");
        logger.info("#             propertyFile          : " + propertyFile);
        logger.info("#             outDataAccessXmlFile  : " + outDataAccessXmlFile);
        logger.info("#             outPersistenceXmlFile : " + outPersistenceXmlFile);
        logger.info("#             outDbProperiesFile    : " + outDbProperiesFile);
        logger.info("#             outDataAccessJavaFile : " + outDataAccessJavaFile);
        logger.info("#             datasourcesCount      : " + datasourcesCount);
        logger.info("#########################################################");

        try {

            Map<String, Map<String, ProjectDbProperty>> dbProperies = new HashMap<String, Map<String, ProjectDbProperty>>();

            if (getPropertyFile() != null) {

                Properties props = new Properties();

                try {
                    props.load(new InputStreamReader(new FileInputStream(propertyFile), "UTF-8"));
                } catch (IOException e) {
                    logger.error("execute", e);
                }


                if (props.isEmpty()) {
                    throw new BuildException("Property file '" + propertyFile + "' is empty");
                }

                dbProperies = getDbProperties(props);
            }

            if (checkProjectDbProperty(dbProperies)) {

                List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries = getEntries(dbProperies);

                Beans springDataAccessXmlData = SpringDatabaseAccessXmlAssist.createDataAccessXmlData(
                        selectPropertyByPropertyTYpe(ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, entries)
                );

                Persistence persistenceXml = HbmPersistenceXmlAssist.createPersistence(
                        selectPropertyByPropertyTYpe(ProjectDbProperty.PROPERTY_TYPE.HIBERNATE, entries)
                );

                if (getOutDataAccessXmlFile() != null) {
                    writeXml(getOutDataAccessXmlFile(), springDataAccessXmlData);
                }

                if (getOutPersistenceXmlFile() != null) {
                    writeXml(getOutPersistenceXmlFile(), persistenceXml);
                }

                if (getOutDbProperiesFile() != null) {
                    writeDbProperties(getOutDbProperiesFile(), selectPropertyByPropertyTYpe(ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, entries));
                }

                if (getOutDataAccessJavaFile() != null) {
                    writeDatabaseAccessJava(getOutDataAccessJavaFile(), entries);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("Exception: " + ex.getMessage());
            throw new BuildException(ex.getMessage());
        }
    }

    private void writeDatabaseAccessJava(String file, List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries) throws Exception {

        logger.info("writeDatabaseAccessJava()=> file: "+file);

        String javaSourceCode = new DataAccessJavaClassGenerator().generate(entries);

        File f = new File(file);
        if (!f.exists()) {
            if (!f.createNewFile()) {
                throw new Exception("Cant't create file - '" + file + "'");
            }
        }

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)));
        pw.println(javaSourceCode);
        pw.close();

    }

    private List<Map.Entry<String, Map<String, ProjectDbProperty>>> getEntries(Map<String, Map<String, ProjectDbProperty>> dbProperies) {

        Map<String, ProjectDbProperty> mainUnit = dbProperies.remove(ProjectDbProperty.MAIN_UNIT_ID);

        List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries = new ArrayList<Map.Entry<String, Map<String, ProjectDbProperty>>>(dbProperies.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Map<String, ProjectDbProperty>>>() {
            @Override
            public int compare(Map.Entry<String, Map<String, ProjectDbProperty>> o1, Map.Entry<String, Map<String, ProjectDbProperty>> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        Integer dsCount = getDatasourcesCount() != null && getDatasourcesCount().length() > 0
                ? Integer.parseInt(getDatasourcesCount())
                : DEFAULT_DS_COUNT;

        if (entries.size() < dsCount) {
            addEmptyUnits(entries, dsCount - entries.size());
        }

        if (mainUnit != null) {
            Collections.reverse(entries);
            entries.add(new HashMap.SimpleEntry<String, Map<String, ProjectDbProperty>>(ProjectDbProperty.MAIN_UNIT_ID, mainUnit));
            Collections.reverse(entries);
        } else {
            Collections.reverse(entries);
            addEmptyUnit(entries, ProjectDbProperty.MAIN_UNIT_ID);
            Collections.reverse(entries);
        }

        return entries;
    }

    private void addEmptyUnits(List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries, int i) {

        for (int key = 1, j = 0; j < i; key++) {

            String dsid = ProjectDbProperty.DS_PREFIX + (key < 10 ? "0" : "") + String.valueOf(key);

            boolean exist = false;
            for (Map.Entry<String, Map<String, ProjectDbProperty>> e : entries) {
                if (e.getKey().equals(dsid)) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                addEmptyUnit(entries, dsid);
                j++;
            }
        }

    }

    private List<Map.Entry<String, Map<String, ProjectDbProperty>>> addEmptyUnit(List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries, String dsid) {

        HashMap<String, ProjectDbProperty> map = new HashMap<String, ProjectDbProperty>();

        map.put("password", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "password", ""));
        map.put("jdbcUrl", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "jdbcUrl", ""));
        map.put("user", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "user", ""));
        map.put("dataSourceClass", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "dataSourceClass", "${database." + dsid + ".datasource.dataSourceClass}"));
        map.put("driverClass", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "driverClass", ""));
        map.put("checkoutTimeout", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "checkoutTimeout", ""));
        map.put("hibernate.dialect", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.HIBERNATE, "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect"));
        map.put("maxIdleTime", new ProjectDbProperty(dsid, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, "maxIdleTime", ""));

        entries.add(new HashMap.SimpleEntry<String, Map<String, ProjectDbProperty>>(dsid, map));

        return entries;
    }

    private void writeDbProperties(String file, List<Map.Entry<String, Map<String, ProjectDbProperty>>> dbProperties) throws Exception {

        logger.info("writeDbProperties()=> file: " + file);

        File f = new File(file);
        if (!f.exists()) {
            if (!f.createNewFile()) {
                throw new Exception("Cant't create file - '" + file + "'");
            }
        }


        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)));

        pw.println("#Database connection properties");
        pw.println("#Generated by CreateDataAccessConfig.java");


        for (Map.Entry<String, Map<String, ProjectDbProperty>> e : dbProperties) {
            pw.println();
            pw.println("########### " + e.getKey().toUpperCase() + " ###########");
            ArrayList<String> names = new ArrayList<String>(e.getValue().keySet());
            Collections.sort(names);
            for (String name : names) {
                String propName = ProjectDbProperty.getPropertyName(e.getKey(), "datasource", name);
                String propValue = ProjectDbProperty.getPropertyName(e.getKey(), ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, name);
                if("dataSourceClass".equalsIgnoreCase(name)) {
                    pw.println(propName + "="+ "com.espendwise.manta.support.spring.EmptyDataSource");
                }  else {
                    pw.println(propName + "=");
                }
            }
        }

        pw.close();

    }

    private void writeXml(String xmlFile, Object data) throws Exception, IOException {

        logger.info("writeXml()=> file: " + xmlFile);

        File f = new File(xmlFile);
        if (!f.exists()) {
            if (!f.createNewFile()) {
                throw new Exception("Cant't create file - '" + xmlFile + "'");
            }
        }

        FileOutputStream out = new FileOutputStream(f);
        JAXBContext context = JAXBContext.newInstance(data.getClass());

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, SPRING_DATA_ACCESS_SCHEMA_LOCATION);

/*        NamespacePrefixMapper mapper = new SpringDataAccessNamespacePrefixMapper();
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);*/

        marshaller.marshal(data, out);

        out.close();
    }

    private static boolean checkProjectDbProperty(java.util.Map<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesMap) throws Exception {

        for (java.util.Map.Entry<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesEntry : dbPropertiesMap.entrySet()) {

            checkProjectDataSourceDbProperty(dbPropertiesEntry.getKey(),
                    selectPropertyByTYpe(ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, dbPropertiesEntry.getValue())
            );

            checkProjectHibernateDbProperty(dbPropertiesEntry.getKey(),
                    selectPropertyByTYpe(ProjectDbProperty.PROPERTY_TYPE.HIBERNATE, dbPropertiesEntry.getValue())
            );

        }

        return true;

    }

    private List<Map.Entry<String, Map<String, ProjectDbProperty>>> selectPropertyByPropertyTYpe(String type, List<Map.Entry<String, Map<String, ProjectDbProperty>>> values) {

        if (values == null) {
            return values;
        }

        List<Map.Entry<String, Map<String, ProjectDbProperty>>> x = new ArrayList<Map.Entry<String, Map<String, ProjectDbProperty>>>(values.size());
        for (Map.Entry<String, Map<String, ProjectDbProperty>> e : values) {
            x.add(new HashMap.SimpleEntry<String, Map<String, ProjectDbProperty>>(e.getKey(), selectPropertyByTYpe(type, e.getValue())));
        }

        return x;
    }

    private static Map<String, ProjectDbProperty> selectPropertyByTYpe(String type, Map<String, ProjectDbProperty> values) {


        if (values == null) {
            return values;
        }

        Map<String, ProjectDbProperty> x = new HashMap<String, ProjectDbProperty>();
        for (Map.Entry<String, ProjectDbProperty> e : values.entrySet()) {
            if (e.getValue().getType().equalsIgnoreCase(type)) {
                x.put(e.getKey(), e.getValue());
            }
        }

        return x;
    }


    private static boolean checkProjectDataSourceDbProperty(String key, Map<String, ProjectDbProperty> values) throws Exception {

        for (String field : ProjectDbProperty.REQURED_DATA_SOURCE_FIELDS) {

            ProjectDbProperty found = null;

            for (ProjectDbProperty v : values.values()) {
                if (field.equalsIgnoreCase(v.getPropertyName())) {
                    found = v;
                    break;
                }
            }

            if (found == null) {
                throw new Exception("Property '"
                        + ProjectDbProperty.getPropertyName(key, ProjectDbProperty.PROPERTY_TYPE.DATASOURCE, field)
                        + "' is requred");
            }
        }

        return true;
    }


    private static boolean checkProjectHibernateDbProperty(String key, Map<String, ProjectDbProperty> values) throws Exception {
        return true;
    }

    private static java.util.Map<String, java.util.Map<String, ProjectDbProperty>> getDbProperties(Properties props) {

        java.util.Map<String, java.util.Map<String, ProjectDbProperty>> dbProperties = new HashMap<String, Map<String, ProjectDbProperty>>();

        Enumeration<?> propertyNames = props.propertyNames();

        if (propertyNames != null) {

            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                if (propertyName.startsWith(ProjectDbProperty.DATA_BASE_PREFIX)) {
                    ProjectDbProperty dbp = parseProjectDbProperty(propertyName, (String) props.get(propertyName));
                    java.util.Map<String, ProjectDbProperty> m = dbProperties.get(dbp.getUbitId());
                    if (m == null) {
                        m = new HashMap<String, ProjectDbProperty>();
                        dbProperties.put(dbp.getUbitId(), m);
                    }
                    m.put(dbp.getPropertyName(), dbp);
                }
            }

        }

        return dbProperties;

    }

    private static ProjectDbProperty parseProjectDbProperty(String propertyName, String propertyValue) {

        List<String> propertyNameParts = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(propertyName, ".");
        while (st.hasMoreElements()) {
            propertyNameParts.add((String) st.nextElement());
        }

        if (propertyNameParts.size() < 4
                || propertyNameParts.get(0).trim().length() == 0
                || propertyNameParts.get(1).trim().length() == 0
                || propertyNameParts.get(2).trim().length() == 0
                || dbPropertyName(propertyNameParts).trim().length() == 0) {

            throw new IllegalArgumentException("wrong property  name format,'" + propertyName + "' ," +
                    " expected:  database.{dbidentifier}.{propertyType(datasource|hibernate)}.{dbPropertyName}," +
                    " example: 'database.1.datasource.url' ");

        }

        String unit = propertyNameParts.get(1).trim();

        return new ProjectDbProperty(unit,
                propertyNameParts.get(2).trim(),
                dbPropertyName(propertyNameParts),
                propertyValue
        );
    }

    private static String dbPropertyName(List<String> propertyNameParts) {
        String s = "";
        int i = 0;
        for (String pnp : propertyNameParts) {
            if (i >= 3) {
                s += s.length() == 0 ? pnp : ("." + pnp);
            }
            i++;
        }
        return s;
    }


/*   public static class SpringDataAccessNamespacePrefixMapper extends NamespacePrefixMapper {

    @Override
    public String getPreferredPrefix(String s, String s1, boolean b) {
        if(s.equals("http://www.springframework.org/schema/aop")){
            return  "aop";
        } else if(s.equals("http://www.springframework.org/schema/tx")){
            return  "tx";
        }  else if(s.equals("http://www.springframework.org/schema/context")){
            return  "context";
        }   else if(s.equals("http://www.springframework.org/schema/beans")){
            return  "";
        }
        return "undefined";
    }
}*/

}