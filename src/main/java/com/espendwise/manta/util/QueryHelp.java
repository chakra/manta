package com.espendwise.manta.util;


import com.espendwise.manta.util.criteria.FilterValue;

import java.util.List;

public class QueryHelp {


    public static final String EQUAL = "=";
    private static final String AND = "and";

    public static String startWith(String value) {
        return escapeQuotes(value) + "%";
    }

    public static String endWith(String value) {
        return escapeQuotes("%" + value);
    }

    public static String contains(String value) {
        return "%" + escapeQuotes(value) + "%";
    }

    public static String toQuoted(String str) {
        StringBuilder sb = new StringBuilder("'");
        sb.append(escapeQuotes(str));
        sb.append('\'');
        return sb.toString();
    }

    public static String escapeQuotes(String str) {
        if (str == null) {
            return Constants.EMPTY;
        }

        int i = str.indexOf('\'');
        if (i == -1) {
            return str;
        }

        char c;
        StringBuilder sb = new StringBuilder(str.substring(0, i));
        while (i < str.length()) {
            c = str.charAt(i);
            if (c == '\'') {
                sb.append('\'');
            }
            sb.append(c);
            i++;
        }

        return sb.toString();
    }

    public static String toFilterValue(FilterValue filter) {
        return toFilterValue(filter.getFilterValue(), filter.getFilterType());
    }

    // Used in query.setParameter and no need to escape quotes
    public static String toFilterValue(String value, String filterType) {
        return Constants.FILTER_TYPE.START_WITH.equals(filterType)
                ? value + "%" : Constants.FILTER_TYPE.CONTAINS.equals(filterType)
                ? "%" + value + "%" : value;
    }

    public static String conditionString(String alias, String field, String condition, String value) {
        return field(alias, field) + condition + toQuoted(value);
    }

    public static String conditionString(String alias, String field, String condition, String alias2, String field2) {
        return field(alias,field) + condition + field(alias2, field2);
    }

    public static String fetchAndCriteria(List<String> l) {
        return Utility.toCommaString(l, Constants.SPACE + AND + Constants.SPACE);
    }

    public static String field(String alias, String f) {
      return alias + Constants.POINT + f;
    }

    public static String in(List<String> l) {
      String s = Constants.EMPTY;
      for(String x:l){
          if(Utility.isSet(s)) { s+=",";}
          s+=toQuoted(x);
      }
        return s;
    }
}
