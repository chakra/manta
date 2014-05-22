package com.espendwise.manta.auth;


import java.io.Serializable;

public class AppContextItem implements Serializable {

    private CtxModel[] dependecy;
    private Object ctxImpl;

    public AppContextItem(CtxModel appCtx) throws IllegalAccessException, InstantiationException {
        this.ctxImpl = appCtx.getCtx().getImplClass().newInstance();
        this.dependecy = appCtx.getDependency();
    }

    public CtxModel[] getDependecy() {
        return dependecy;
    }

    public void setDependecy(CtxModel[] dependecy) {
        this.dependecy = dependecy;
    }

    public Object getCtxImpl() {
        return ctxImpl;
    }

    public void setCtxImpl(Object ctxImpl) {
        this.ctxImpl = ctxImpl;
    }
}
