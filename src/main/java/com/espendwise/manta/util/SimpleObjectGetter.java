package com.espendwise.manta.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Implemetation for fast access to object fields
 * Used only for single properties
 */
public class SimpleObjectGetter extends ObjectGetter {

    public SimpleObjectGetter(Object o, String fieldExp) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(o, fieldExp);
    }

    public Object getTarget(Object obj) {
        if (obj instanceof SelectableObjects.SelectableObject) {
            return ((SelectableObjects.SelectableObject) obj).getValue();
        } else if (obj instanceof SiteListViewSelectableObjects.SelectableObject){
            return ((SiteListViewSelectableObjects.SelectableObject) obj).getValue();
        } else {
            return obj;
        }
    }

    @Override
    public String toString() {
        return "SimpleSortObjectGette{super=" + super.toString() + '}';
    }

}
