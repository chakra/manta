package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.Ctx;

import java.io.Serializable;
import java.util.Locale;

public interface IAppUser extends Serializable, Cloneable {

    public Locale getLocale();

    public <T> T getContext(Ctx<T> ctx);

    public String getUserName();

    public Long getUserId();

    public String getUserTypeCd();

    public boolean isSystemAdmin();

    public boolean isStoreAdmin();

    public boolean isAdmin();

    public boolean isQneOfAdmin();
}
