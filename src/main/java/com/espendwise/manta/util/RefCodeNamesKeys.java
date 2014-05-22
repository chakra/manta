package com.espendwise.manta.util;


import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class RefCodeNamesKeys {

    private static final Logger logger = Logger.getLogger(RefCodeNamesKeys.class);
   
    public static List<Pair<String, String>> getRefCodeValues(Class refcode) {
       return  getRefCodeValues(refcode, true);
    }

    public static List<Pair<String, String>> getRefCodeValues(Class refcode, boolean sortByValue) {

        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();

        try {

            if (refcode == null) {
                return values;
            }

            Field[] fields = refcode.getFields();

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                    values.add(new Pair<String, String>(field.getName(), (String) field.get(null)));
                }
            }
        } catch (Exception e) {
            logger.error("getRefCodeValues()=> ERROR: " + e.getMessage(), e);
            values.clear();
        }

        if (!values.isEmpty() && sortByValue) {
            Collections.sort(values, new Comparator<Pair<String, String>>() {
                @Override
                public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                    return Utility.strNN(o1.getObject2()).compareTo(Utility.strNN(o2.getObject2()));
                }
            });
        }

        return values;
    }

    public static <T extends Pair> Map toMap(List<T> pList, boolean valuesToKeys) {

        Map<Object, Object> map = new HashMap<Object, Object>();
        if (pList == null) {
            return map;
        }

        for (T oData : pList) {
            if (oData != null) {
                if (valuesToKeys) {
                    map.put(oData.getObject2(), oData.getObject1());
                } else {
                    map.put(oData.getObject1(), oData.getObject2());
                }
            }
        }
        return map;
    }

    public static List<Pair<String, String>> getRefCodeValues(String code) {

        Class<?>[] classes = RefCodeNames.class.getDeclaredClasses();

        for (Class<?> f : classes) {

            if (f.getSimpleName().equals(code)) {
                return getRefCodeValues(f);
            }
        }
        return null;
    }

    public static String findRefCodeByValue(String impl, String value) {
       return (String) toMap(getRefCodeValues(impl), true).get(value);
    }
}
