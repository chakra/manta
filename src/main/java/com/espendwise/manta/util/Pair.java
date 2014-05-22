package com.espendwise.manta.util;

import java.io.Serializable;

public class Pair<O1, O2> implements Serializable {

    public static final String OBJECT2 = "object2";
    public static final String OBJECT1 = "object1";

    private O1 object1;
    private O2 object2;

    public Pair() {
    }

    public Pair(O1 object1, O2 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public O1 getObject1() {
        return object1;
    }

    public void setObject1(O1 object1) {
        this.object1 = object1;
    }

    public O2 getObject2() {
        return object2;
    }

    public void setObject2(O2 object2) {
        this.object2 = object2;
    }
}