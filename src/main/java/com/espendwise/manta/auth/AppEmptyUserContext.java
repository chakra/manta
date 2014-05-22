package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppUserContext;
import com.espendwise.manta.auth.ctx.Ctx;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;

import java.util.HashMap;

public class AppEmptyUserContext extends AppUserContext {

    public AppEmptyUserContext() throws ApplicationRuntimeException {
        try {
            ctxHolder = new HashMap<Ctx, AppContextItem>();
        } catch (Exception e) {
             throw new ApplicationRuntimeException(e);
        }
    }

    @Override
    protected void createContextModel() throws InstantiationException, IllegalAccessException {

    }
}
