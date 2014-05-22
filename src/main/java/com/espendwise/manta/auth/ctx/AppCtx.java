package com.espendwise.manta.auth.ctx;

import java.io.Serializable;

public interface AppCtx extends Serializable {

    public static final Ctx<AppDomainContext> DOMAIN = new Ctx<AppDomainContext>(AppDomainContext.class);
    public static final Ctx<AppStoreContext> STORE = new Ctx<AppStoreContext>(AppStoreContext.class);
    public static final Ctx<AppOrcaInstanceContext> ORCA_INSTANCE = new Ctx<AppOrcaInstanceContext>(AppOrcaInstanceContext.class);
    public static final Ctx<AppStjohnInstanceContext> STJOHN_INSTANCE = new Ctx<AppStjohnInstanceContext>(AppStjohnInstanceContext.class);
    public static final Ctx<AppAccountContext> ACCOUNT = new Ctx<AppAccountContext>(AppAccountContext.class);
    public static final Ctx<AppSiteContext> SITE = new Ctx<AppSiteContext>(AppSiteContext.class);


}
