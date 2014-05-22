package com.espendwise.manta.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Class that may be used to facilitate the task of creating jsp pages that need to have selectable
 * elements to them.  This class will take care of the matching and state as far as what was originally
 * set and what is set now.
 */
public class SelectableObjects<T> {

    private static Log logger = LogFactory.getLog(SelectableObjects.class);

    private List<SelectableObject<T>> values;

    public SelectableObjects() {
        values = new ArrayList<SelectableObject<T>>();
    }

    public SelectableObjects(List<T> allOptions, List<T> selected,  Comparator<T> pOptionalComp) {
        selectableObjects(allOptions, selected, pOptionalComp);
    }

    public SelectableObjects(List<T> allOptions, List<T> selected,  Comparator<T> pOptionalComp, int showConfOnlyFl) {
        if (showConfOnlyFl == 0) { //configured and non-configured List of elements
            selectableObjects( allOptions, selected,  pOptionalComp);
        } else { //configured only List of elements configured and non-configured List of elements
            selectableAndConfObjects(allOptions, selected,  pOptionalComp);
        }
    }

    public SelectableObjects(List<T> allOptions, List<T> selected, Comparator<T> pOptionalComp, boolean selectOnlyTrue) {
        selectableObjects( allOptions, selected, pOptionalComp, selectOnlyTrue);
    }

    public Iterator<SelectableObject<T>> getIterator() {
    	Iterator<SelectableObject<T>> returnValue = null;
    	if (Utility.isSet(values)) {
    		returnValue = values.iterator();
    	}
    	return returnValue;
    }

    protected void selectableObjects( List<T> allOptions, List<T> selected, Comparator<T> pOptionalComp, boolean selectOnlyTrue) {

        if (selectOnlyTrue) {

            values = new ArrayList<SelectableObject<T>>();

            for (T o : allOptions) {

                SelectableObject<T> newEntry = new SelectableObject<T>();

                if (selected != null) {

                    for (T selectedObj : selected) {

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

    protected void selectableObjects(List<T> allOptions, List<T> selected, Comparator<T> pOptionalComp) {

        values = new ArrayList<SelectableObject<T>>();

        for (T o : allOptions) {

            SelectableObject<T> newEntry = new SelectableObject<T>();
            newEntry.setValue(o);

            if (selected != null) {

                for (T selectedObj : selected) {

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

    protected void selectableAndConfObjects(List<T> allOptions, List<T> selected,  Comparator<T> pOptionalComp) {
        int fl = 0;
        values = new ArrayList<SelectableObject<T>>();
        for (T o : allOptions) {
            SelectableObject<T> newEntry = new SelectableObject<T>();
            newEntry.setValue(o);
            if (selected != null) {
                fl = 0;
                for (T selectedObj : selected) {
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

    public T getValue(int idx) {
        if (idx > values.size()) {
            return null;
        }
        return values.get(idx).getValue();
    }

    public void setValue(int idx, T pValue) {
        int len = values.size();
        SelectableObject<T> sel;
        if (len <= idx) {
            while (len < idx) {
                sel = new SelectableObject<T>();
                values.add(sel);
            }
            sel = new SelectableObject<T>();
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
            values.add(new SelectableObject<T>());
            listLength++;
        }
        values.get(idx).setSelected(pValue);
    }

    public List<T> getNewlySelected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (sel.isSelected() && !sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<T> getNewlyDeselected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (!sel.isSelected() && sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<T> getSelected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (sel.isSelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<T> getDeselected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (!sel.isSelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<T> getOriginalSelected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }


    public List<T> getOriginalDeselected() {
        ArrayList<T> returnList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            if (!sel.isOriginallySelected()) {
                if (sel.getValue() != null) {
                    returnList.add(sel.getValue());
                }
            }
        }
        return returnList;
    }

    public List<T> getValues() {
        ArrayList<T> valueList = new ArrayList<T>();
        for (SelectableObject<T> sel : values) {
            valueList.add(sel.getValue());
        }
        return valueList;
    }


    public List<SelectableObject<T>> getOriginalValues() {
        setOriginalyState();
        return values;
    }

    public List<SelectableObject<T>> getSelectableObjects() {
        return values;
    }

    public void resetState() {
        for (SelectableObject<T> sel : values) {
            sel.setOriginallySelected(sel.isSelected());
        }
    }

    public void setOriginalyState() {
        for (SelectableObject<T> sel : values) {
            sel.setSelected(sel.isOriginallySelected());
        }
    }

    public void unSelect() {
        for (SelectableObject sel : values) {
            sel.setSelected(false);
        }
    }

    public static class SelectableObject<T> {

        private boolean mOriginallySelected;
        private boolean mSelected;
        private T mValue;

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

        public T getValue() {
            return mValue;
        }

        public void setValue(T pValue) {
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
        return "SelectableObjects{" +
                "values=" + values +
                '}';
    }
}
