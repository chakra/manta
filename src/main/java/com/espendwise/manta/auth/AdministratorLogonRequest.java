package com.espendwise.manta.auth;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;

import java.util.Locale;

public class AdministratorLogonRequest extends ApplicationLogonRequest {

    public AdministratorLogonRequest(String serverName, AuthenticationUserMap authUserMap, Locale locale, AuthUserAccessTokenProperty accessToken) {
        super(serverName,
                authUserMap,
                locale,
                Utility.toMap(Constants.ACCESS_TOKEN, ((Object) accessToken))
        );
    }


    public  AuthUserAccessTokenProperty getAuthAccessTokenProperty(){
       return (AuthUserAccessTokenProperty) getAdditionalParameter(ADDITIONAL_PARAMETERS.ACCESS_TOKEN);
    }


}
