package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.Pair;

import java.util.*;


public class SiteHierarchyLevelItemsForm extends TreeMap<Integer, List<Pair<Long, String>>> {

   public SiteHierarchyLevelItemsForm() {
    }

    public SiteHierarchyLevelItemsForm(Map<? extends Integer, ? extends List<Pair<Long, String>>> m) {
        super(m);
    }

    public SiteHierarchyLevelItemsForm(SortedMap<Integer, ? extends List<Pair<Long, String>>> m) {
        super(m);
    }

    public SiteHierarchyLevelItemsForm(Comparator<? super Integer> comparator) {
        super(comparator);
    }

    @Override
    public List<Pair<Long, String>> get(Object key) {
        List<Pair<Long, String>> value = super.get(key);
        if (value == null) {
            value = new ArrayList<Pair<Long, String>>();
            super.put((Integer) key, value);
        }

        return value;
    }


}
