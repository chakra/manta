package com.espendwise.manta.util;

import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Class which contains methods foe getting values from collection object by java bean path
 * Used cglib for fast access to class fields
 */
public  abstract class ObjectGetter {

    private Object obj;
    private String fieldExp;

    public String field;
    public FastClass clazz;
    public FastMethod method;
    public Object collectionKey;


    protected ObjectGetter(Object o, String fieldExp) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.obj = o;
        this.fieldExp = fieldExp;
    }

    public void init() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    	init(null);
    }
    
    public void init(Class clazz) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        String field;
        Object key = null;

        if (fieldExp.indexOf("[") > 0) {
            field = fieldExp.substring(0, fieldExp.indexOf("["));
            key = Integer.parseInt(fieldExp.substring(fieldExp.indexOf("[") + 1, fieldExp.indexOf("]")));
        } else if (fieldExp.indexOf("(") > 0) {
            field = fieldExp.substring(0, fieldExp.indexOf("("));
            key = fieldExp.substring(fieldExp.indexOf("(") + 1, fieldExp.indexOf(")"));
        } else {
            field = fieldExp;
        }

        Class itemClass = null;
        if (clazz == null) {
            Object targetObject = getTarget(obj);
            itemClass = targetObject.getClass();
        }
        else {
        	itemClass = clazz;
        }

        this.field = field;
        this.clazz = FastClass.create(itemClass);
        this.method = this.clazz.getMethod(Utility.getJavaGetterName(field), new Class[0]);
        this.collectionKey = key;

    }


    public Object get(Object obj) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!isInit()) {
            throw new IllegalAccessException();
        }

        if (obj == null) {
            return null;
        }

        Object target = getTarget(obj);
        if (target == null) {
            return null;
        }

        Object valueObj = method.invoke(target, new Class[0]);

        if (collectionKey != null) {
            if (valueObj.getClass().isArray()) {
                return ((Object[]) valueObj)[(Integer) collectionKey];
            } else if (valueObj instanceof Map) {
                return ((Map) valueObj).get(collectionKey);
            } else if (valueObj instanceof Collection) {
                return ((Collection) valueObj).toArray(new Object[0])[(Integer) collectionKey];
            }
        }

        return valueObj;
    }


    protected abstract Object getTarget(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    @Override
    public String toString() {
        return "SortObjectGetter{" +
                ", field='" + field + '\'' +
                ", clazz=" + clazz +
                ", method=" + method +
                ", collectionKey=" + collectionKey +
                '}';
    }

    public boolean isInit() {
        return this.clazz != null && this.method != null;
    }

    public void destroy() {
        field = null;
        clazz = null;
        method = null;
        collectionKey = null;
    }

}
