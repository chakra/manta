package com.espendwise.manta.util;

import com.espendwise.manta.model.view.SiteListView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SiteListViewSelectableObjects implements Serializable {
    
    private static Log logger = LogFactory.getLog(SiteListViewSelectableObjects.class);

    private List<SelectableObject> values;

    public SiteListViewSelectableObjects() {
        values = new ArrayList<SelectableObject>();
    }

    public SiteListViewSelectableObjects(List<SiteListView> allOptions, List<SiteListView> selected,  Comparator<SiteListView> pOptionalComp) {
        selectableObjects(allOptions, selected, pOptionalComp);
    }

    public SiteListViewSelectableObjects(List<SiteListView> allOptions, List<SiteListView> selected,  Comparator<SiteListView> pOptionalComp, int showConfOnlyFl) {
        if (showConfOnlyFl == 0) { //configured and non-configured List of elements
            selectableObjects( allOptions, selected,  pOptionalComp);
        } else { //configured only List of elements configured and non-configured List of elements
            selectableAndConfObjects(allOptions, selected,  pOptionalComp);
        }
    }

    public SiteListViewSelectableObjects(List<SiteListView> allOptions, List<SiteListView> selected, Comparator<SiteListView> pOptionalComp, boolean selectOnlySiteListViewrue) {
        selectableObjects(allOptions, selected, pOptionalComp, selectOnlySiteListViewrue);
    }


    protected void selectableObjects( List<SiteListView> allOptions, List<SiteListView> selected, Comparator<SiteListView> pOptionalComp, boolean selectOnlySiteListViewrue) {

        if (selectOnlySiteListViewrue) {

            values = new ArrayList<SelectableObject>();

            for (SiteListView o : allOptions) {

                SelectableObject newEntry = new SelectableObject();

                if (selected != null) {

                    for (SiteListView selectedObj : selected) {

                        if (pOptionalComp == null) {

                            if (o.equals(selectedObj)) {
                                newEntry.setValue(o);
                                newEntry.setSelected(true);
                                newEntry.setOriginallySelected(true);
                                values.add(newEntry);
                            }
                        } else {

                            if (pOptionalComp.compare(o, selectedObj) == 0) {
                                newEntry.setValue(o);
                                newEntry.setSelected(true);
                                newEntry.setOriginallySelected(true);
                                values.add(newEntry);
                            }
                        }
                    }
                }
            }
        } else {
            selectableObjects(allOptions, selected, pOptionalComp);
        }
    }

    protected void selectableObjects(List<SiteListView> allOptions, List<SiteListView> selected, Comparator<SiteListView> pOptionalComp) {

        values = new ArrayList<SelectableObject>();

        for (SiteListView o : allOptions) {

            SelectableObject newEntry = new SelectableObject();
            newEntry.setValue(o);

            if (selected != null) {

                for (SiteListView selectedObj : selected) {

                    if (pOptionalComp == null) {

                        if (o.equals(selectedObj)) {
                            newEntry.setSelected(true);
                            newEntry.setOriginallySelected(true);
                        }

                    } else {

                        if (pOptionalComp.compare(o, selectedObj) == 0) {
                            newEntry.setSelected(true);
                            newEntry.setOriginallySelected(true);
                        }

                    }

                }

            }

            values.add(newEntry);
        }

    }

    protected void selectableAndConfObjects(List<SiteListView> allOptions, List<SiteListView> selected,  Comparator<SiteListView> pOptionalComp) {
        int fl = 0;
        values = new ArrayList<SelectableObject>();
        for (SiteListView o : allOptions) {
            SelectableObject newEntry = new SelectableObject();
            newEntry.setValue(o);
            if (selected != null) {
                fl = 0;
                for (SiteListView selectedObj : selected) {
                    if (pOptionalComp == null) {
                        if (o.equals(selectedObj)) {
                            newEntry.setSelected(true);
                            newEntry.setOriginallySelected(true);
                            fl = 1;
                        }
                    } else {
                        if (pOptionalComp.compare(o, selectedObj) == 0) {
                            newEntry.setSelected(true);
                            newEntry.setOriginallySelected(true);
                            fl = 1;
                        }
                    }
                }
            }

            if (fl == 1) {
                values.add(newEntry);
            }

        }

    }

    public SiteListView getValue(int idx) {
        if (idx > values.size()) {
            return null;
        }
        return values.get(idx).getValue();
    }

    public void setValue(int idx, SiteListView pValue) {
        int len = values.size();
        SelectableObject sel;
        if (len <= idx) {
            while (len < idx) {
                sel = new SelectableObject();
                values.add(sel);
            }
            sel = new SelectableObject();
            values.add(sel);
        } else {
            sel = values.get(idx);
        }
        sel.setValue(pValue);
    }

    public boolean getSelected(int idx) {
        return idx <= values.size() && values.get(idx).isSelected();
    }

    public void setSelected(int idx, boolean pValue) {
        int listLength = values.size();
        while (idx >= listLength) {
            values.add(new SelectableObject());
            listLength++;
        }
        values.get(idx).setSelected(pValue);
    }

    public List<SiteListView> getNewlySelected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (sel.isSelected() && !sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<SiteListView> getNewlyDeselected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (!sel.isSelected() && sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<SiteListView> getSelected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (sel.isSelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<SiteListView> getDeselected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (!sel.isSelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<SiteListView> getOriginalSelected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<SiteListView> getOriginalDeselected() {
        ArrayList<SiteListView> returnList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            if (!sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<SiteListView> getValues() {
        ArrayList<SiteListView> valueList = new ArrayList<SiteListView>();
        for (SelectableObject sel : values) {
            valueList.add(sel.getValue());
        }
        return valueList;
    }


    public List<SelectableObject> getOriginalValues() {
        setOriginalyState();
        return values;
    }

    public List<SelectableObject> getSelectableObjects() {
        return values;
    }

    public void resetState() {
        for (SelectableObject sel : values) {
            sel.setOriginallySelected(sel.isSelected());
        }
    }

    public void setOriginalyState() {
        for (SelectableObject sel : values) {
            sel.setSelected(sel.isOriginallySelected());
        }
    }

    public void unSelect() {
        for (SelectableObject sel : values) {
            sel.setSelected(false);
        }
    }

    public static class SelectableObject {

        private boolean mOriginallySelected;
        private boolean mSelected;
        private SiteListView mValue;

        public SelectableObject() {
        }

        public boolean isSelected() {
            return mSelected;
        }

        public boolean getSelected() {
            return isSelected();
        }

        public void setSelected(boolean pSelected) {
            mSelected = pSelected;
        }

        public boolean isOriginallySelected() {
            return mOriginallySelected;
        }

        public void setOriginallySelected(boolean pOriginallySelected) {
            mOriginallySelected = pOriginallySelected;
        }

        public SiteListView getValue() {
            return mValue;
        }

        public void setValue(SiteListView pValue) {
            mValue = pValue;
        }

        @Override
        public String toString() {
            return "SelectableObject{" +
                    "mOriginallySelected=" + mOriginallySelected +
                    ", mSelected=" + mSelected +
                    ", mValue=" + mValue +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "SiteListViewSelectableObjects{" +
                "values=" + values +
                '}';
    }
}
