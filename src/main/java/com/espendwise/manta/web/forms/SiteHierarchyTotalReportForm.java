package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;

import java.util.*;


public class SiteHierarchyTotalReportForm extends TreeMap<String, List<SiteHierarchyTotalReportView>> {

    public SiteHierarchyTotalReportForm() {
        super();
    }

    public SiteHierarchyTotalReportForm(Map<? extends String, ? extends List<SiteHierarchyTotalReportView>> m) {
        super(m);
    }

    public SiteHierarchyTotalReportForm(Comparator<? super String> comparator) {
        super(comparator);
    }

    public SiteHierarchyTotalReportForm(SortedMap<String, ? extends List<SiteHierarchyTotalReportView>> m) {
        super(m);
    }

    @Override
    public List<SiteHierarchyTotalReportView> get(Object key) {
        List<SiteHierarchyTotalReportView> obj = super.get(key);
        if (obj == null) {
            obj = new ArrayList<SiteHierarchyTotalReportView>();
            super.put((String) key, obj);
        }
        return obj;
    }

}
