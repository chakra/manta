package com.espendwise.manta.i18n;


import com.espendwise.manta.auth.AppUser;

import java.util.Locale;

public interface UserMessageResource extends MessageResource {

    public  String getMessage(String key, Object[] args);

    public String getMessage(String key);

    public String getMessageOrNull(String key);

    public String getMessageOrDefault(String key, String pDefaultValue);

    public String getMessage(String key, Object[] args, boolean returnNullIfNotFound);

    public String getMessage(String key, Object[] args, boolean returnNullIfNotFound, Locale locale);

    public String getMessage(AppUser appUser, String key, Object[] args, Locale preferedLocale, boolean returnNullIfNotFound);

    public String getMessage(String s, Object[] objects, Locale locale);


}
