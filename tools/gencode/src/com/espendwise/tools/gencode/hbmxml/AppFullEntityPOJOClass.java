package com.espendwise.tools.gencode.hbmxml;


import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Column;
import org.hibernate.tool.hbm2x.Cfg2JavaTool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;


public class AppFullEntityPOJOClass  extends AppEntityPOJOClass {

    private java.util.regex.Pattern endWithIdDigitPattern = Pattern.compile(".*_ID(\\d)?$");
    private java.util.regex.Pattern endWithDigitPattern = Pattern.compile(".*(\\d)$");
    public static final Set<String> IGNORED_IMPORTS = new HashSet<String>(AppEntityPOJOClass.IGNORED_IMPORTS);
    static {
        IGNORED_IMPORTS.add("com.espendwise.manta.dto.TableObject");
    }

    public AppFullEntityPOJOClass(PersistentClass persistentClass, Cfg2JavaTool cfg2JavaTool, Configuration pCfg2HbmTool, String pSerialVesrionUID) {
        super(persistentClass, pCfg2HbmTool, cfg2JavaTool, pSerialVesrionUID);

    }

    public Iterator getFKKeysIterator() {
        PersistentClass classMapping = cfg.getClassMapping(super.getMappedClassName());
        return classMapping.getTable().getForeignKeyIterator();
    }

    public boolean isPkForFk(Property pProperty, ForeignKey pKey) {
        if (pProperty.getValue().getColumnIterator().hasNext()) {
            if (((Column) pProperty.getValue().getColumnIterator().next()).getName().equals(pKey.getColumn(0).getName())) {
                return true;
            }
        }
        return false;
    }

    public String getFKConstantValue(Property pProperty, ForeignKey pKey) {
        PersistentClass classMappings = cfg.getClassMapping(pKey.getReferencedEntityName());
        Property property = classMappings.getIdentifierProperty();
        return pProperty.getName() + "." + property.getName();

    }

    @Override
    public String generateImports() {
        return filterImports(super.generateImports(), IGNORED_IMPORTS);
    }

    public String getConstantField(Property pProperty) {

        String name = super.getConstantField(pProperty.getName());
        if ((pProperty.getType().isAssociationType()
                || pProperty.getType().isCollectionType()
                || pProperty.getType().isEntityType())
                && endWithIdDigitPattern.matcher(name).matches()) {
            String tname = name.substring(0, name.length() - 3);
            if (endWithDigitPattern.matcher(name.toUpperCase()).matches()) {
                name = tname + name.substring(name.length() - 1, name.length());
            } else {
                name = tname;
            }
        }

        return name;
    }

    @Override
    public Set<String> getIgnoredImports() {
        return IGNORED_IMPORTS;
    }
}