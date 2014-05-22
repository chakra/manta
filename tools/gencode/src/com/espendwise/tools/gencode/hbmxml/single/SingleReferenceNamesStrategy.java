package com.espendwise.tools.gencode.hbmxml.single;

import com.espendwise.tools.gencode.dbxml.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

import java.util.List;
import java.util.Properties;

public class SingleReferenceNamesStrategy extends DelegatingReverseEngineeringStrategy {

    private final static Log log = LogFactory.getLog(SingleReferenceNamesStrategy.class);

    public static final String APP_DB_PREFIX[]   = {"CLW_", "ESW_", "LWO_"};
    public static final String APP_WO_DB_PREFIX[]   = {"WO_"};
    public static final String APP_JAVA_SUFFIX = "Data";

    public SingleReferenceNamesStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    public String tableToClassName(TableIdentifier pTableIdentifier) {

        boolean woTable = false;

        String name = super.tableToClassName(pTableIdentifier);
        String _class = getClass(name);
        String _package = getPackage(name);

        for (String prefix : APP_WO_DB_PREFIX) {
            if (pTableIdentifier.getName().startsWith(prefix) || pTableIdentifier.getName().startsWith(prefix.toLowerCase())) {
                woTable = true;
                break;
            }
        }

        if (!woTable) {
            for (String prefix : APP_DB_PREFIX) {
                if (pTableIdentifier.getName().startsWith(prefix) || pTableIdentifier.getName().startsWith(prefix.toLowerCase())) {
                    _class = _class.substring(prefix.length() - 1);
                     break;
                }
            }
        }

        if (!_class.toUpperCase().endsWith(getJavaSuffix())) {
            _class = _class + getJavaSuffix();
        }

        return _package.length() > 0 ? _package + "." + _class : _class;
    }

    public String getClass(String pName) {
        int packageIdx = pName.lastIndexOf(".");
        return !(packageIdx < 0) ? pName.substring(packageIdx + 1) : pName;
    }

    public String getPackage(String pName) {
        int packageIdx = pName.lastIndexOf(".");
        return !(packageIdx < 0) ? pName.substring(0, packageIdx) : "";
    }

    public boolean excludeForeignKeyAsCollection(String string, TableIdentifier tableIdentifier, List list, TableIdentifier tableIdentifier1, List list1) {
        return true;
    }

    public boolean excludeForeignKeyAsManytoOne(String string, TableIdentifier tableIdentifier, List list, TableIdentifier tableIdentifier1, List list1) {
        return true;
    }

    public String columnToPropertyName(TableIdentifier table, String column) {

        for (String prefix : APP_WO_DB_PREFIX) {
            if (column.toUpperCase().startsWith(prefix)) {
                return super.columnToPropertyName(table, column.substring(prefix.length()));
            }
        }

        for (String prefix : APP_DB_PREFIX) {
            if (column.toUpperCase().startsWith(prefix)) {
                return super.columnToPropertyName(table, column.substring(prefix.length()));
            }
        }

        return super.columnToPropertyName(table, column);
    }

    public Properties getTableIdentifierProperties(TableIdentifier identifier) {
        Properties p = new Properties();
        p.put("sequence", identifier.getName().toUpperCase() + "_SEQ");
        return p;
    }

    public String getTableIdentifierStrategyName(TableIdentifier identifier) {
        return "sequence";
    }

    public String getJavaSuffix() {
        return APP_JAVA_SUFFIX;
    }

    public String tableToClassName(Table t) {
        return tableToClassName(new TableIdentifier(t.getName()));
    }
}


