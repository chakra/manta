package com.espendwise.manta.auth.ctx;


import com.espendwise.manta.auth.AppContextItem;
import com.espendwise.manta.auth.CtxModel;

import java.io.Serializable;
import java.util.Map;

public abstract class AppUserContext implements Serializable {

    protected Map<Ctx, AppContextItem> ctxHolder;

    public AppContextItem remove(Ctx key) {
        AppContextItem obj = ctxHolder.remove(key);
        if (obj.getDependecy() != null) {
            for (CtxModel x : obj.getDependecy()) {
                remove(x.getCtx());
            }
        }
        return obj;
    }


    protected abstract void createContextModel() throws InstantiationException, IllegalAccessException;

    public <T> T get(Ctx<T> ctx) {
        AppContextItem item = ctxHolder.get(ctx);
        if (item != null) {
            return (T) item.getCtxImpl();
        } else {
            return null;
        }
    }


    public void add(AppUserContext context) {
        ctxHolder.putAll(context.ctxHolder);

    }
}
