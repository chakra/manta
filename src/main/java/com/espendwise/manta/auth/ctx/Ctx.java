package com.espendwise.manta.auth.ctx;

import java.io.Serializable;

public class Ctx<T> implements Serializable {

    private Class<T> implClass;

    public Ctx(Class<T> x) {
        this.implClass = x;
    }

    public Class<T> getImplClass() {
        return implClass;
    }
}