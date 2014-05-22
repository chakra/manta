package com.espendwise.manta.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Universal solution for getting values from object by field name/path
 * Slowing than SimpleSortObjectGetter but could getting value by java bean path
 * Ex. field name could be next : fieldName1.fieldName2[x].fieldName2 ... etc
 */
public class PathObjectGetter extends ObjectGetter {

    private String[] targetPath;

    public PathObjectGetter(Object o, String[] path) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        super(o, path[path.length - 1]);
        this.targetPath = Arrays.copyOfRange(path, 0, path.length - 1);
    }

    public Object getTarget(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Object targetObj = obj;

        for (String p : getTargetPath()) {
            SimpleObjectGetter getter = new SimpleObjectGetter(targetObj, p);
            getter.init();
            targetObj = getter.get(targetObj);
            getter.destroy();
            if (targetObj == null) {
                break;
            }

        }

        return targetObj;

    }

    public String[] getTargetPath() {
        return targetPath;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public String toString() {
        return "PathSortObjectGetter{" +
                "super=" + super.toString() +
                ", targetPath=" + (targetPath == null ? null : Arrays.asList(targetPath)) +
                '}';
    }
}