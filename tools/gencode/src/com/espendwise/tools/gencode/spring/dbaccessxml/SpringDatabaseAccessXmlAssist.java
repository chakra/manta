package com.espendwise.tools.gencode.spring.dbaccessxml;


import com.espendwise.tools.gencode.dbxml.DbXmlAssist;
import com.espendwise.tools.gencode.hbmxml.persistence.HbmPersistenceXmlAssist;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.aop.AspectjAutoproxy;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.*;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.context.ComponentScan;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.context.PropertyPlaceholder;
import com.espendwise.tools.gencode.spring.dbaccessxml.schema.tx.AnnotationDriven;
import com.espendwise.tools.gencode.util.ProjectDbProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.Map;

public class SpringDatabaseAccessXmlAssist {
    
    private final static Log logger = LogFactory.getLog(DbXmlAssist.class);

    public static final String DEFAULT_DATASOUCE_DESTROY_METHOD = "close";
    public static final String TX_MODE = "aspectj";

    public static final String DEFAUL_ENTITY_MANAGER_FACTORY_CLASS = "org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean";
    public static final String DEFAUL_TX_ENTITY_MANAGER_CLASS = "org.springframework.orm.jpa.JpaTransactionManager";
    public static final String DEFAULT_TRANSICTIONAL_ASPECT_CLASS = "com.espendwise.manta.support.spring.aop.TransactionalAspect";
    public static final String DEFAUL_TX_MANAGER_CLASS = "com.espendwise.manta.support.spring.ApplicationTransactionManager";

    public static final String MANTA_BASE_PACKAGE = "com.espendwise.manta";
    public static final String DATASOUCE_NAME_PREFIX = "dataSource";
    public static final String ENTITY_MANAGER_FACTORY_ID_PREFIX = "entityManagerFactory";
    public static final String TRANSACTION_ENTITY_MANAGER_ID_PREFIX = "transactionManager";
    public static final String TRANSACTION_MANAGER_ID = "transactionManager";
    public static final String ENTITY_MANAGER_PROPERTY_ID_PREFIX = "entityManager";

    public static final String ENTITY_MANAGER_FACTORY_PERSISTEN_UNIT_PROPERTY_NAME = "persistenceUnitName";
    public static final String ENTITY_MANAGER_FACTORY_DATASOURCE_PROPERTY_NAME = "dataSource";
    public static final String TX_MANAGER_ENTITY_MANAGER__FACTORY_PROPERTY_NAME = "entityManagerFactory";
    public static final String TX_MANAGER_PROPERTY_NAME = "transactionManager";
    public static final String TX_MANAGER_LIST_PROPERTY_NAME = "managers";
    public static final String DEFAULT_PROPERTY_LOCATION = "classpath*:META-INF/spring/*.properties";

    public static Beans createDataAccessXmlData(List<Map.Entry<String,Map<String,ProjectDbProperty>>> entries)  {

        com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.Beans beans = new com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.ObjectFactory().createBeans();

        List<Bean> dataSources = createDataSources(entries);

        PropertyPlaceholder propertyPlaceholdern = createPropertyPlaceholder();
        ComponentScan componentScan = createContextComponentScan();
        AnnotationDriven txAnnotationSupport = createTxAnnotationSupport();
        AspectjAutoproxy aopSuppor = createAopSupport();
        List<Bean> tx = createTx(entries);
        Bean txAspect  = createTxAspect();

        beans.getAspectjAutoproxyOrBeanOrComponentScan().add(propertyPlaceholdern);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().add(txAnnotationSupport);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().add(aopSuppor);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().add(componentScan);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().addAll(dataSources);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().addAll(tx);
        beans.getAspectjAutoproxyOrBeanOrComponentScan().add(txAspect);

        return beans;

    }

    private static PropertyPlaceholder createPropertyPlaceholder() {
        PropertyPlaceholder p = new com.espendwise.tools.gencode.spring.dbaccessxml.schema.context.ObjectFactory().createPropertyPlaceholder();
        p.setLocation(DEFAULT_PROPERTY_LOCATION);
        return p;
    }

    private static Bean createTxAspect() {

        Bean bean = new ObjectFactory().createBean();

        bean.setClazz(DEFAULT_TRANSICTIONAL_ASPECT_CLASS);

        Property p = new ObjectFactory().createProperty();
        p.setName(TX_MANAGER_PROPERTY_NAME);
        p.setRef(TRANSACTION_MANAGER_ID);

        bean.getProperty().add(p);

        return  bean;
    }


    private static List<Bean> createTx(List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries) {
       
        List<Bean>  tx = new ArrayList<Bean>();
      
        for (java.util.Map.Entry<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesEntry : entries) {
            tx.add(createrEntityManagerFactory(dbPropertiesEntry.getKey()));
        }

        for (java.util.Map.Entry<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesEntry : entries) {
            tx.add(createrTxEntityManager(dbPropertiesEntry.getKey()));
        }


        tx.add(createrTxManager(entries));

        return tx;
    }



    private static Bean createrEntityManagerFactory(String key) {

        Bean bean = new ObjectFactory().createBean();

        bean.setClazz(DEFAUL_ENTITY_MANAGER_FACTORY_CLASS);
        bean.setId(getName(ENTITY_MANAGER_FACTORY_ID_PREFIX, key));

        Property persistenceUnitReference = new ObjectFactory().createProperty();
        persistenceUnitReference.setName(ENTITY_MANAGER_FACTORY_PERSISTEN_UNIT_PROPERTY_NAME);
        persistenceUnitReference.setValue(HbmPersistenceXmlAssist.PERSISTENCE_UNIT_PREFIX + key);

        Property datasourceReference = new ObjectFactory().createProperty();
        datasourceReference.setName(ENTITY_MANAGER_FACTORY_DATASOURCE_PROPERTY_NAME);
        datasourceReference.setRef(getName(DATASOUCE_NAME_PREFIX, key));

        bean.getProperty().add(persistenceUnitReference);
        bean.getProperty().add(datasourceReference);

        return bean;
    }

    private static Bean createrTxEntityManager(String key) {

        Bean bean = new ObjectFactory().createBean();

        bean.setClazz(DEFAUL_TX_ENTITY_MANAGER_CLASS);
        bean.setId(getName(TRANSACTION_ENTITY_MANAGER_ID_PREFIX, key));

        Property emftReference = new ObjectFactory().createProperty();
        emftReference.setName(TX_MANAGER_ENTITY_MANAGER__FACTORY_PROPERTY_NAME);
        emftReference.setRef(getName(ENTITY_MANAGER_FACTORY_ID_PREFIX, key));

        bean.getProperty().add(emftReference);

        return bean;
    }

    private static Bean createrTxManager(List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries) {

        Bean bean = new ObjectFactory().createBean();

        bean.setClazz(DEFAUL_TX_MANAGER_CLASS);
        bean.setId(TRANSACTION_MANAGER_ID);

        Property p = new ObjectFactory().createProperty();
        p.setName(TX_MANAGER_LIST_PROPERTY_NAME);

        com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.Map m = new ObjectFactory().createMap();

        for (java.util.Map.Entry<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesEntry : entries) {
            Entry e = new ObjectFactory().createEntry();
            e.setKey(getName(HbmPersistenceXmlAssist.PERSISTENCE_UNIT_PREFIX, dbPropertiesEntry.getKey()));
            e.setValueRef(getName(TRANSACTION_ENTITY_MANAGER_ID_PREFIX, dbPropertiesEntry.getKey()));
            m.getEntry().add(e);
        }

        p.setMap(m);
        bean.getProperty().add(p);

        return  bean;

    }

 
    private static AspectjAutoproxy createAopSupport() {
      return new com.espendwise.tools.gencode.spring.dbaccessxml.schema.aop.ObjectFactory().createAspectjAutoproxy();
    }

    private static ComponentScan createContextComponentScan() {
        ComponentScan componentScan = new com.espendwise.tools.gencode.spring.dbaccessxml.schema.context.ObjectFactory().createComponentScan();
        componentScan.setBasePackage(MANTA_BASE_PACKAGE);
        return  componentScan;
    }

    private static AnnotationDriven createTxAnnotationSupport() {
        AnnotationDriven txSupport = new com.espendwise.tools.gencode.spring.dbaccessxml.schema.tx.ObjectFactory().createAnnotationDriven();
        txSupport.setMode(TX_MODE);
        return  txSupport;
    }


    private static List<Bean> createDataSources(List<Map.Entry<String, Map<String, ProjectDbProperty>>> entries) {

        List<Bean> datasources = new ArrayList<Bean>();

        
        for (java.util.Map.Entry<String, java.util.Map<String, ProjectDbProperty>> dbPropertiesEntry : entries) {

            com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.ObjectFactory factory = new com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.ObjectFactory();

            Bean bean = factory.createBean();

            ProjectDbProperty sourceClass = dbPropertiesEntry.getValue().get(ProjectDbProperty.DATA_SOURCE_CLASS);
            String sourceClassValue = sourceClass == null ? ProjectDbProperty.BASE_DS_CLASS : sourceClass.getValue();

            bean.setDestroyMethod(DEFAULT_DATASOUCE_DESTROY_METHOD);
            bean.setId(getName(DATASOUCE_NAME_PREFIX, dbPropertiesEntry.getKey()));
            bean.setClazz(sourceClassValue);

            List<ProjectDbProperty> props = new ArrayList<ProjectDbProperty>(dbPropertiesEntry.getValue().values());
            Collections.sort(props, new Comparator<ProjectDbProperty>() {
                @Override
                public int compare(ProjectDbProperty o1, ProjectDbProperty o2) {
                    return o1.getPropertyName().compareTo(o2.getPropertyName());
                }
            });

            for (ProjectDbProperty dbprop : props) {

                if (!ProjectDbProperty.DATA_SOURCE_CLASS.equals(dbprop.getPropertyName())) {

                    com.espendwise.tools.gencode.spring.dbaccessxml.schema.beans.Property prop = factory.createProperty();

                    prop.setName(dbprop.getPropertyName());
                    prop.setValue("${" +
                            ProjectDbProperty.getPropertyName(
                                    dbPropertiesEntry.getKey(),
                                    "datasource",
                                    dbprop.getPropertyName()
                            ) + "}");

                    bean.getProperty().add(prop);
                }

            }

            datasources.add(bean);
        }

        return datasources;

    }

    public static String getName(String datasouceNamePrefix, String key) {
        return datasouceNamePrefix == null || datasouceNamePrefix.length() == 0 
                ? key
                :datasouceNamePrefix + key.substring(0, 1).toUpperCase() + key.substring(1);
    }


}
