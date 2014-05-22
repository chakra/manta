package com.espendwise.manta.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class Hierarchy<K, V> extends HashMap<K, Hierarchy<K, V>> {

    private static Log logger = LogFactory.getLog(Hierarchy.class);

    private V mValue;
    private K mKey;

    public Hierarchy() {
        super();
    }

    public Hierarchy(K pKey, V pValue) {
        super();
        this.mKey = pKey;
        this.mValue = pValue;
    }

    protected V getTopParent(K pKey, List<V> pPath) {
        K key = pKey;
        Hierarchy<K, V> parent;
        int i = 0;
        do {
            parent = getParentEntry(key, pPath);
            if (parent != null) {
                key = parent.getKey();
            }
            i++;
        } while (parent != null || i > 10000);

        return !pPath.isEmpty() ? pPath.get(pPath.size() - 1) : null;
    }

    private V getParent(K pKey, List<V> pPath) {
        Hierarchy<K, V> parent = getParentEntry(pKey, pPath);
        return parent != null ?  !pPath.isEmpty() ? pPath.get(0) :  null : null;
    }

    public V getTopCategory(K pKey) {
        return getTopParent(pKey, new ArrayList<V>());
    }

    public V getParent(K pKey) {
        return getParent(pKey, new ArrayList<V>());
    }


    private Hierarchy<K, V> getParentEntry(K pKey, List<V> pPath) {

        if (this.containsKey(pKey)) {
            if (getKey() == null) {
                pPath.add(this.get(pKey).getValue());
            } else {
                pPath.add(this.getValue());
            }
            return this;
        }

        for (Hierarchy<K, V> child : values()) {
            if (child != null) {
                Hierarchy<K, V> value = child.getParentEntry(pKey, pPath);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    protected void subSet(List<V> pSet, List<List<V>> pSubSet) {
        pSet.add(this.getValue());
        if (values().isEmpty()) {
            pSubSet.add(pSet);
        } else {
            for (Hierarchy<K, V> v : values()) {
                v.subSet(new ArrayList<V>(pSet), pSubSet);
            }
        }
    }

    public void addChild(Hierarchy<K, V> pChildParentsMap) {
        this.put(pChildParentsMap.getKey(), pChildParentsMap);
    }

    public K getKey() {
        return this.mKey;
    }

    @Override
    public String toString() {
        return "Hierarchy{" +
                "mKey=" + mKey +
                ", mValue=" + mValue +
                ", childs=" + values() +
                '}';
    }

    public  V getValue() {
        return mValue;
    }


    public void setValue(V value) {
        this.mValue = value;
    }

    public List<V> getAllValues() {

        List<V> list = new ArrayList<V>();

        Collection<Hierarchy<K, V>> childs = this.values();
        if (this.getValue() != null) {
            list.add(this.getValue());
        }

        for (Hierarchy<K, V> child : childs) {
            list.addAll(child.getAllValues());
        }

        return list;
    }


}