package com.espendwise.manta.auth;


import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.model.view.DisplaySettingsView;
import com.espendwise.manta.service.DatabaseAccess;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private static final Logger logger = Logger.getLogger(UserAuthenticationProvider.class);

    private SaltSource saltSource;
    private UserAuthenticationService userAuthenticationService;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
    private AdministratorLogonRoles roles;

    public UserAuthenticationProvider() {
    }

    public final void afterPropertiesSet() throws Exception {
        doAfterPropertiesSet();
    }

    protected void doAfterPropertiesSet() throws Exception {
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {

        Object salt = null;

        if (getSaltSource() != null) {
            salt = getSaltSource().getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("exception.authentication.badCredentials");
        }

        String presentedPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
        if (!getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salt)) {
            throw new BadCredentialsException("exception.authentication.badCredentials");
        }

        if(Utility.isSet(getRoles())){
              if(!isAuthorized(userDetails.getAuthorities())){
                  throw new BadAuthorizedRoleException("exception.authentication.badAuthorizedRole");
              }
          }


    }

    public boolean isAuthorized(Collection<? extends GrantedAuthority> credentials) {
        for (GrantedAuthority x : credentials) {
            for (String access : getRoles().getList()) {
                if (x.getAuthority().equals(access)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        logger.info("authenticate()=> BEGIN");

         checkInput(authentication);

        // Determine username
        String username = (authentication.getPrincipal() == null) ? AuthConst.NONE_PROVIDED : authentication.getName();
        logger.info("authenticate()=> username: " + username);

        AuthenticationUserMap authUserMap = retrieveAuthUserMap(authentication, username);

        //does the mapping between the objects.  We use the active flag for all status indicators.
        Collection<GrantedAuthority> authorities = getAuthorities(authUserMap.values());
        String password = !authUserMap.isEmpty() ? getAuthUserPassword(authUserMap) : AuthConst.NONE_PROVIDED;
        boolean enabled = !authorities.isEmpty();

        UserDetails user = new User(username,
                password,
                enabled,
                enabled,
                enabled,
                enabled,
                authorities
        );


        AuthUserAccessTokenProperty authToken = authentication instanceof AccessTokenUsernameAuthenticationToken
                ? ((AccessTokenUsernameAuthenticationToken) authentication).getAuthToken()
                : null;

        logger.info("authenticate()=>  create auth user");

        AuthUser authUser = new AuthUser(
                user,
                null,
                new AuthUserContext(authUserMap, authToken,  new DisplaySettingsView())
        );

        logger.info("authenticate()=>  check auth user");

        check(authUser, (UsernamePasswordAuthenticationToken) authentication);

        logger.info("authenticate()=> auth user is valid, create success authentication");

        authentication = this.createSuccessAuthentication(authentication, authUser);

        logger.info("authenticate()=> END, OK! authentication - " + authentication);

        return authentication;

    }

    private void checkInput(Authentication authentication) {

        String username = (authentication.getPrincipal() == null) ? AuthConst.NONE_PROVIDED  : authentication.getName();
        if (username == null || username.trim().length() == 0) {
            throw new BadCredentialsException("exception.authentication.badCredentials");
        }


        String presentedPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
        if (presentedPassword == null || presentedPassword.trim().length() == 0) {
            throw new BadCredentialsException("exception.authentication.badCredentials");
        }
    }

    private AuthenticationUserMap retrieveAuthUserMap(Authentication authentication, String username) {

        logger.info("retrieveAuthUserMap()=> BEGIN");

        AuthenticationUserMap authUserMap = new AuthenticationUserMap();

        Map<String, AuthDatabaseAccessUnit> dataAccessUnits = getUserDataAccessUnits(username);
        logger.info("retrieveAuthUserMap()=> dataAccessUnits: " + dataAccessUnits);

        for (AuthDatabaseAccessUnit unit : dataAccessUnits.values()) {

            logger.info("retrieveAuthUserMap()=> retrieve user for unit : " + unit);
            AuthUserDetails user = retrieveUser(unit.getUnitName(), username);

            if (user != null) {
                logger.info("retrieveAuthUserMap()=> check user : " + user);
                if (check(user, (UsernamePasswordAuthenticationToken) authentication)) {
                    List<StoreListEntity> userStores = getUserStores(unit, user, getUnitStoreIds(unit.getMainStoreIdents()));
                    //temporary solution, need to remove when we haved db structure with global id
                    overrideGlobalEntityId(selectDsUnitIndex(unit), userStores);
                    logger.info("retrieveAuthUserMap()=> user('" + username + "') is valid, unit : " + unit.getUnitName());
                    authUserMap.put(unit, new AuthUserData(user, userStores));
                } else {
                    logger.info("retrieveAuthUserMap()=> user('" + username + "') is not valid, unit : " + unit.getUnitName());
                }
            } else {
                logger.info("retrieveAuthUserMap()=> user('" + username + "') is  not found, unit : " + unit.getUnitName());
            }

        }

        logger.info("retrieveAuthUserMap()=> authUserMap: " + authUserMap);
        logger.info("retrieveAuthUserMap()=> END.");

        return authUserMap;
    }

    //temporary solution, need to remove when we haved db structure with global id
    private int selectDsUnitIndex(AuthDatabaseAccessUnit unit) {
        for (int i = 0; i < DatabaseAccess.availableUnits().length; i++) {
            if (DatabaseAccess.availableUnits()[i].equals(unit.getUnitName())) {
                return i + 1;
            }
        }
        return 0;
    }

    //temporary solution, need to remove when we haved db structure with global id
    private void overrideGlobalEntityId(int unitIndex, List<StoreListEntity> userStores) {
        for (StoreListEntity store : userStores) {
            store.setGlobalEntityId((10000000000L + (store.getStoreId() * 1000) + unitIndex));
        }
    }

    private List<Long> getUnitStoreIds(List<AuthMainStoreIdent> globalDbStores) {

        if (globalDbStores == null) {
            return null;
        }
        List<Long> x = new ArrayList<Long>();

        for (AuthMainStoreIdent storeIdent: globalDbStores) {
            x.add(storeIdent.getStoreId());
        }

        return x;
    }

    private String getAuthUserPassword(AuthenticationUserMap authUserMap) {
        return authUserMap.values().iterator().next().getUserDetails().getPassword();
    }

    private Collection<GrantedAuthority> getAuthorities(Collection<AuthUserData> values) {

        Collection<GrantedAuthority> x = new ArrayList<GrantedAuthority>();

        for (AuthUserData details : values) {
            x.addAll(details.getUserDetails().getAuthorities());
        }

        if (x.isEmpty()) {
            x.add(new GrantedAuthorityImpl(AuthConst.ROLE_UNDEFINED));
        }

        return x;
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, AuthUser user) {


        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user,
                authentication.getCredentials(),
                user.getAuthorities()
        );

        result.setDetails(authentication.getDetails());

        return result;
    }

    private boolean check(AuthUserDetails user, UsernamePasswordAuthenticationToken authentication) throws RuntimeException {

        try {

            getPreAuthenticationChecks().check(user);
            additionalAuthenticationChecks(user, authentication);
            getPostAuthenticationChecks().check(user);

        } catch (BadAuthorizedRoleException e) {
            logger.info("check()=> check failed, problem: "+e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.info("check()=> check failed, problem: "+e.getMessage());
            return false;
        }

        return true;
    }

    private void check(AuthUser user, UsernamePasswordAuthenticationToken authentication) throws RuntimeException {
        preAuthenticationChecks.check(user);
        additionalAuthenticationChecks(user, authentication);
        postAuthenticationChecks.check(user);
    }

    protected final AuthUserDetails retrieveUser(String datasource, String username) {
        try {
            return getUserAuthenticationService().loadUserByUsername(datasource, username);
        } catch (DataAccessException repositoryProblem) {
            return null;
        } catch (AuthenticationException repositoryProblem) {
            return null;
        }
    }

    private Map<String, AuthDatabaseAccessUnit> getUserDataAccessUnits(String username) {
        return getUserAuthenticationService().findUserDataAccessUnits(username);
    }

    private List<StoreListEntity> getUserStores(AuthDatabaseAccessUnit unit, AuthUserDetails user, List<Long> storeIds) {
        return getUserAuthenticationService().findUserStores(unit.getAuthDataSourceIdent().getDataSourceName(),
                user,
                storeIds
        );
    }

    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public UserAuthenticationService getUserAuthenticationService() {
        return userAuthenticationService;
    }

    public void setUserAuthenticationService(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    public SaltSource getSaltSource() {
        return saltSource;
    }

    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    public void setRoles(AdministratorLogonRoles roles) {
         this.roles = roles;
    }

    public AdministratorLogonRoles getRoles() {
        return roles;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        public void check(UserDetails user) {

            if (!user.isAccountNonLocked()) {
                throw new LockedException("exception.authentication.locked", user);
            }

            if (!user.isEnabled()) {
                throw new DisabledException("exception.authentication.disabled", user);
            }

            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("exception.authentication.expired", user);
            }

        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("exception.authentication.credentialsExpired", user);
            }
        }
    }

    public UserDetailsChecker getPreAuthenticationChecks() {
        return preAuthenticationChecks;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    public UserDetailsChecker getPostAuthenticationChecks() {
        return postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}