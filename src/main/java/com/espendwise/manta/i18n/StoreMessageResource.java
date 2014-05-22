package com.espendwise.manta.i18n;

import java.util.Locale;

public interface StoreMessageResource extends MessageResource{

    public String getMessage(Long storeId, Locale locale, String key);

    public String getMessage(Long storeId, Locale locale, String key, Object[] args);

}
