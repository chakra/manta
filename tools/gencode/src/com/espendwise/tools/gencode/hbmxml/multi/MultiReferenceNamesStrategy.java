package com.espendwise.tools.gencode.hbmxml.multi;

import com.espendwise.tools.gencode.hbmxml.single.SingleReferenceNamesStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategyUtil;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.Column;
import org.hibernate.util.StringHelper;

import java.beans.Introspector;
import java.util.List;

public class MultiReferenceNamesStrategy extends SingleReferenceNamesStrategy {

    private final static Log log = LogFactory.getLog(MultiReferenceNamesStrategy.class);

    public static final String APP_JAVA_SUFFIX = "FullEntity";

    public MultiReferenceNamesStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    public String getPackage(String pName) {
        int packageIdx = pName.lastIndexOf(".");
        if (!(packageIdx < 0)) {
            packageIdx = pName.substring(0, packageIdx).lastIndexOf(".");
            return !(packageIdx < 0) ? pName.substring(0, packageIdx) + ".fullentity" : "";
        } else {
            return "";
        }
    }

    public boolean excludeForeignKeyAsCollection(String string, TableIdentifier tableIdentifier, List list, TableIdentifier tableIdentifier1, List list1) {
        return false;
    }

    public boolean excludeForeignKeyAsManytoOne(String string, TableIdentifier tableIdentifier, List list, TableIdentifier tableIdentifier1, List list1) {
        return false;
    }

    public String getJavaSuffix() {
        return APP_JAVA_SUFFIX;
    }

    public String foreignKeyToEntityName(String keyname, TableIdentifier fromTable, List fromColumnNames, TableIdentifier referencedTable, List referencedColumnNames, boolean uniqueReference) {
        if (fromColumnNames != null && fromColumnNames.size() == 1) {
            return columnToPropertyName(fromTable, ((Column) fromColumnNames.get(0)).getName());
        } else {
            return keyname;
        }
    }

    public String foreignKeyToCollectionName(String keyname, TableIdentifier fromTable, List fromColumns, TableIdentifier referencedTable, List referencedColumns, boolean uniqueReference) {

        String propertyName = Introspector.decapitalize(StringHelper.unqualify(tableToClassName(fromTable)));

        if (propertyName.endsWith(getJavaSuffix())) {
            propertyName = propertyName.substring(0, propertyName.length() - getJavaSuffix().length());
        }

        propertyName = ReverseEngineeringStrategyUtil.simplePluralize(propertyName);

        if (!uniqueReference) {
            if (fromColumns != null && fromColumns.size() == 1) {
                String columnName = ((Column) fromColumns.get(0)).getName();
                propertyName = propertyName + "For" + ReverseEngineeringStrategyUtil.toUpperCamelCase(columnName);
            } else { // composite key or no columns at all safeguard
                propertyName = propertyName + "For" + ReverseEngineeringStrategyUtil.toUpperCamelCase(keyname);
            }
        }

        return propertyName;
    }



}