package com.espendwise.tools.gencode.hbmxml;

import org.hibernate.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HbmAssist {

    public static DbBaseType2SqlType.SqlType getSqlType(DbBaseTypeSettings settings, String type) {
        return  DbBaseType2SqlType.convert(settings, type);
    }

    public static String quote(String columnName) {
        if(columnName==null) return columnName;
        if(needQuote(columnName)) {
            if(columnName.length()>1 && columnName.charAt(0)=='`' && columnName.charAt(columnName.length()-1)=='`') {
                return columnName; // avoid double quoting
            }
            return "`" + columnName + "`";
        } else {
            return columnName;
        }
    }

    public static boolean needQuote(String name) {
        return name != null && (name.indexOf('-') > 0 || name.indexOf(' ') > 0);
    }


    public static boolean intBounds(int size) {
        return size>=0 && size!=Integer.MAX_VALUE;
    }
    public static int toInt(String s) {
        return Integer.parseInt(s);
    }

    public static boolean isTrue(String pValue) {
        return  Boolean.parseBoolean(pValue);
    }


    public static void mergeMultiMap(Map<Object, List<Object>> dest, Map<Object, List<Object>> src) {

        for (Map.Entry<Object, List<Object>> element : src.entrySet()) {
            List<Object> existing = dest.get(element.getKey());
            if (existing == null) {
                dest.put(element.getKey(), element.getValue());
            } else {
                existing.addAll(element.getValue());
            }
        }

    }

    public static boolean equalTable(Table table1, Table table2) {
        return  table1.getName().equals(table2.getName())
                && ( equal(table1.getSchema(), table2.getSchema() )
                && ( equal(table1.getCatalog(), table2.getCatalog() ) ) );
    }

    private static boolean equal(String str, String str2) {
        return str != null && str.equals(str2) || str2 != null && str2.equals(str) || str == null && str2 == null;
    }

    public static void addToMultiMap(Map<Object, List<Object>> multimap, String key, Object item) {
        List<Object> existing = multimap.get(key);
        if(existing == null) {
            existing = new ArrayList<Object>();
            multimap.put(key, existing);
        }
        existing.add(item);
    }

}
