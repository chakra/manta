package com.espendwise.manta.auth;


import com.espendwise.manta.model.view.DisplaySettingsView;

import java.util.HashMap;

public class AuthUserContext extends HashMap<String, Object> {

    public static final Class<DisplaySettingsView> DISPlAY_SETTINGS = DisplaySettingsView.class;
    public static final Class<AuthenticationUserMap> AUTHI_USER_MAP = AuthenticationUserMap.class;
    public static final Class<AuthUserAccessTokenProperty> AUTH_USER_ACCESS_TOKEN_PROPERTY = AuthUserAccessTokenProperty.class;

    public AuthUserContext(AuthenticationUserMap authUserMap, AuthUserAccessTokenProperty authUserAccessTokenProperty, DisplaySettingsView displaySettings) {
        super();
        put(DISPlAY_SETTINGS.getName(), displaySettings);
        put(AUTHI_USER_MAP.getName(), authUserMap);
        put(AUTH_USER_ACCESS_TOKEN_PROPERTY.getName(), authUserAccessTokenProperty);
    }


    public <T> T get(Class<T> key) {
        return (T) get(key.getName());
    }
}
