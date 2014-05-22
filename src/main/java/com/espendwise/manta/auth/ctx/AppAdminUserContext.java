package com.espendwise.manta.auth.ctx;


import com.espendwise.manta.auth.AppContextItem;
import com.espendwise.manta.auth.AppEmptyUserContext;
import com.espendwise.manta.auth.CtxModel;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;

public class AppAdminUserContext extends AppEmptyUserContext {

    public AppAdminUserContext() throws ApplicationRuntimeException {
        super();
        createContextModel();
}

    @Override
    protected void createContextModel() throws  ApplicationRuntimeException {
        try {
            ctxHolder.put(CtxModel.STORE.getCtx(), new AppContextItem(CtxModel.STORE));
            ctxHolder.put(CtxModel.ORCA_INSTANCE.getCtx(), new AppContextItem(CtxModel.ORCA_INSTANCE));
            ctxHolder.put(CtxModel.STJOHN_INSTANCE.getCtx(), new AppContextItem(CtxModel.STJOHN_INSTANCE));
        } catch (Exception e) {
           throw new ApplicationRuntimeException(e);
        }
    }

    public AppStoreContext getStoreContext() {
        return get(AppCtx.STORE);
    }

}
