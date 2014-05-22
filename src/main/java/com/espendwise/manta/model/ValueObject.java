package com.espendwise.manta.model;


public class ValueObject {

    private boolean dirty;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {

        this.dirty = dirty;
    }
}
