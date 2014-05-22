package com.espendwise.tools.gencode.hbmxml;

import com.espendwise.tools.gencode.dbxml.Database;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.type.Type;

public class DbBaseConfiguration extends Configuration {

    private static final Log log = LogFactory.getLog(DbBaseConfiguration.class);
    private ReverseEngineeringStrategy revEngStrategy = new DefaultReverseEngineeringStrategy();
    private Database database;

    private boolean ignoreconfigxmlmapppings = true;
    private boolean preferBasicCompositeIds = true;
    private String dialect;


    public void read() {
        DbBaseXmlBinder binder = new DbBaseXmlBinder(this, createMappings(), revEngStrategy);
        binder.readFromDatabase(buildMapping(this));
    }

    static private Mapping buildMapping(final Configuration cfg) {

        return new Mapping() {
            /**
             * Returns the identifier type of a mapped class
             */
            public Type getIdentifierType(String persistentClass) throws MappingException {
                PersistentClass pc = cfg.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                return pc.getIdentifier().getType();
            }

            public String getIdentifierPropertyName(String persistentClass) throws MappingException {
                final PersistentClass pc = cfg.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                if (!pc.hasIdentifierProperty()) return null;
                return pc.getIdentifierProperty().getName();
            }

            public Type getReferencedPropertyType(String persistentClass, String propertyName) throws MappingException {
                final PersistentClass pc = cfg.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                Property prop = pc.getProperty(propertyName);
                if (prop == null)
                    throw new MappingException("property not known: " + persistentClass + '.' + propertyName);
                return prop.getType();
            }
        };
    }

    public boolean preferBasicCompositeIds() {
        return preferBasicCompositeIds;
    }

    public void setPreferBasicCompositeIds(boolean flag) {
        preferBasicCompositeIds = flag;
    }

    public boolean isIgnoreconfigxmlmapppings() {
        return ignoreconfigxmlmapppings;
    }

    public void setIgnoreconfigxmlmapppings(boolean ignoreconfigxmlmapppings) {
        this.ignoreconfigxmlmapppings = ignoreconfigxmlmapppings;
    }

    protected void parseMappingElement(Element subelement, String name) {
        if (!isIgnoreconfigxmlmapppings()) {
            super.parseMappingElement(subelement, name);
        } else {
            log.info("parseMappingElement()=> Ignoring " + name + " mapping");
        }
    }

    public void setReverseEngineeringStrategy(ReverseEngineeringStrategy reverseEngineeringStrategy) {
        this.revEngStrategy = reverseEngineeringStrategy;
    }

    public ReverseEngineeringStrategy getReverseEngineeringStrategy() {
        return revEngStrategy;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDialect() {
        return dialect;
    }
}


