package com.espendwise.manta.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.Arrays;

public class AuthConst {

    public static final String ROLE_UNDEFINED = "ROLE_UNDEFINED";
    public static final String NONE_PROVIDED = "NONE_PROVIDED";
    public static final String GUEST = "GUEST";
    public static final String ROLE_ANONUMOUS = "ROLE_ANONUMOUS";

    public static final AuthUser ANONYMOUS =
            new AuthUser(
                    new AuthUserDetails(null,
                            null,
                            null,
                            Arrays.asList(new GrantedAuthority[]{new GrantedAuthorityImpl(ROLE_ANONUMOUS)}),
                            NONE_PROVIDED,
                            GUEST,
                            true,
                            true,
                            true,
                            true
                    ), null, null);

}
