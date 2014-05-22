package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.Ctx;

public enum CtxModel {
   
    DOMAIN(AppCtx.DOMAIN, (CtxModel[]) null),
    STORE(AppCtx.STORE, new CtxModel[]{DOMAIN}),
    ORCA_INSTANCE(AppCtx.ORCA_INSTANCE, new CtxModel[]{STORE}),
    STJOHN_INSTANCE(AppCtx.STJOHN_INSTANCE, new CtxModel[]{STORE}),
    ACCOUNT(AppCtx.ACCOUNT, new CtxModel[]{STORE}),
    SITE(AppCtx.SITE, new CtxModel[]{ACCOUNT});

    private Ctx ctx;
    private CtxModel[] dependency;

    public Ctx getCtx() {
        return ctx;
    }

    public void setCtx(Ctx ctx) {
        this.ctx = ctx;
    }

    public CtxModel[] getDependency() {
        return dependency;
    }

    public void setDependency(CtxModel[] dependency) {
        this.dependency = dependency;
    }

    CtxModel(Ctx ctx, CtxModel[] dependency) {
        this.ctx = ctx;
        this.dependency = dependency;
    }
}
