package com.espendwise.manta.web.util;


import com.espendwise.manta.util.Constants;

import java.beans.PropertyEditorSupport;

public class PatternEditor extends PropertyEditorSupport {

    private final String pattern;
    private final boolean emptyAsNull;


    public PatternEditor(String pattern, boolean emptyAsNull) {
        this.pattern = pattern;
        this.emptyAsNull = emptyAsNull;
    }


    @Override
    public void setAsText(String text) {

        if (text == null) {

            setValue(null);

        } else {

            String value = text.trim();
            if (this.pattern != null && text.equals(pattern)) {
                value = Constants.EMPTY;
            }

            if (this.emptyAsNull && Constants.EMPTY.equals(value)) {
                setValue(null);
            }  else {
                setValue(value);
            }
        }

    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.toString() : Constants.EMPTY);
    }
}
