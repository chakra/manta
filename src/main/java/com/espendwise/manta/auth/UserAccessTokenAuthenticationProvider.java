package com.espendwise.manta.auth;


import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UserAccessTokenAuthenticationProvider extends UserAuthenticationProvider {

    private static final Logger logger = Logger.getLogger(UserAccessTokenAuthenticationProvider.class);


    protected void checkAccessTokenInput(Authentication authentication) {

        AccessTokenAuthenticationToken specificAuthentication = ((AccessTokenAuthenticationToken) authentication);

        if (!Utility.isSet((String) specificAuthentication.getCredentials())) {
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.badCredentials");
        }

        if (!Utility.isSet((String) specificAuthentication.getDatasource())) {
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.wrongDatasourceName");
        }

    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        logger.info("authenticate()=> BEGIN");

        checkAccessTokenInput(authentication);

        AccessTokenAuthenticationToken specificAuthentication = ((AccessTokenAuthenticationToken) authentication);

        Pair<AuthUserDetails, AuthUserAccessTokenProperty> authUserDetails = getUserAuthenticationService().loadUserByAccessToken(
                specificAuthentication.getDatasource(),
                (String) specificAuthentication.getCredentials()
        );

        check(authUserDetails.getObject1(), (AccessTokenAuthenticationToken) authentication);

        logger.info("authenticate()=> auth user is valid, create access token success authentication");

        authentication =this. createSuccessAuthentication(authentication, authUserDetails);
        if (authentication != null) {
            authentication =  super.authenticate(authentication);
        }

        logger.info("authenticate()=> END, OK!");

        return authentication;
    }
    protected Authentication createSuccessAuthentication( Authentication authentication, Pair<AuthUserDetails, AuthUserAccessTokenProperty> user) {


        AccessTokenUsernameAuthenticationToken result = new AccessTokenUsernameAuthenticationToken(
                user.getObject1().getUsername(),
                user.getObject1().getPassword(),
                user.getObject1().getAuthorities(),
                user.getObject2()
        );

        result.setDetails(authentication.getDetails());

        return result;
    }

    private void check(AuthUserDetails authUserDetails, AccessTokenAuthenticationToken authentication) {
        getPreAuthenticationChecks().check(authUserDetails);
        getPostAuthenticationChecks().check(authUserDetails);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }


}
