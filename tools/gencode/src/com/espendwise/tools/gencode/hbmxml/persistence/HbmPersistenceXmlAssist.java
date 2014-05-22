package com.espendwise.tools.gencode.hbmxml.persistence;


import com.espendwise.tools.gencode.util.ProjectDbProperty;

import java.lang.reflect.Field;
import java.util.*;

public class HbmPersistenceXmlAssist {


    public static final String PERSISTENCE_UNIT_PREFIX = "";
    public static final String DEFAULT_PROVIDER = "org.hibernate.ejb.HibernatePersistence";
    public static final String DEFAULT_TRANSACTION_TYPE = "RESOURCE_LOCAL";
    private static final String DEFAULT_VALIDATION_MODE = "NONE";

    private static final String VERSION = "2.0";

    private static interface UNIT_PROPS {
        public static final String[] HIBERNATE_VALIDATOR_APPLY_TO_DDL = new String[]{"hibernate.validator.apply_to_ddl", "false"};
        public static final String[] HIBERNATE_EJB_NAMING_STRATEGY = new String[]{"hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy"};
        public static final String[] HIBERNATE_CONNECTION_CHARSET = new String[]{"hibernate.connection.charSet", "UTF-8"};
        public static final String[] HIBERNATE_FORMAT_SQL = new String[]{"hibernate.format_sql", "true"};
        public static final String[] HIBERNATE_SHOW_SQL = new String[]{"hibernate.show_sql", "true"};
        public static final String[] HIBERNATE_DIALECT = new String[]{"hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect"};
        public static final String[] HIBERNATE_CONNECTION_V8_COMPABILITY = new String[]{"hibernate.connection.oracle.jdbc.V8Compatible", "true"};
    }

    public static Persistence createPersistence(List<Map.Entry<String,Map<String,ProjectDbProperty>>> dbProperies) throws Exception {

        ObjectFactory objectFactory = new ObjectFactory();

        Persistence persistence = objectFactory.createPersistence();
        persistence.setVersion(VERSION);
        persistence.getPersistenceUnit().addAll(createPersistenceUnits(objectFactory, dbProperies));

        return persistence;
    }


    private static List<PersistenceUnit> createPersistenceUnits(ObjectFactory factory, List<Map.Entry<String,Map<String,ProjectDbProperty>>> dbProperies) throws Exception {

        List<PersistenceUnit> units = new ArrayList<PersistenceUnit>();

        for (Map.Entry<String, Map<String, ProjectDbProperty>> key : dbProperies) {
            units.add(createUnit(factory, key.getKey(), key.getValue()));
        }

        return units;
    }


    private static PersistenceUnit createUnit(ObjectFactory factory, String unitName, Map<String, ProjectDbProperty> properties) throws Exception {

        PersistenceUnit unit = factory.createPersistenceUnit();


        unit.setName(PERSISTENCE_UNIT_PREFIX + unitName);
        unit.setNonJtaDataSource(new ObjectFactory().createNonJtaDataSource());
        unit.setProvider(DEFAULT_PROVIDER);
        unit.setTransactionType(DEFAULT_TRANSACTION_TYPE);
        unit.setProperties(createUnitProperties(factory, properties));
        //unit.setValidationMode(createValidationMode());

        return unit;
    }

    private static ValidationMode createValidationMode() {
        ValidationMode validationMode = new ObjectFactory().createValidationMode();
        validationMode.setValue(DEFAULT_VALIDATION_MODE);
        return  validationMode;
    }

    private static Properties createUnitProperties(ObjectFactory factory, Map<String, ProjectDbProperty> projectDbProperties) throws Exception {

        Set<String> propertiesSet = new HashSet<String>();
        Properties properties = factory.createProperties();

        Field[] fields = UNIT_PROPS.class.getDeclaredFields();

        for (Field f : fields) {

            String[] value = (String[]) f.get(new String[]{});

            String propName = value[0];
            String propValue = projectDbProperties.containsKey(propName)
                    ? projectDbProperties.get(propName).getValue()
                    : value[1];

            properties.getProperty().add(createProperty(factory, propName, propValue));

            propertiesSet.add(propName);

        }

        for (Map.Entry<String, ProjectDbProperty> e : projectDbProperties.entrySet()) {

            String propName = e.getKey();
            String propValue = e.getValue().getValue();

            if (!propertiesSet.contains(propName)) {
                properties.getProperty().add(createProperty(factory, propName, propValue));
                propertiesSet.add(propName);
            }

        }

        return properties;
    }

    private static Property createProperty(ObjectFactory factory, String propName, String propValue) {

        Property p = factory.createProperty();

        p.setName(propName);
        p.setAttrValue(propValue);

        return p;
    }


}
