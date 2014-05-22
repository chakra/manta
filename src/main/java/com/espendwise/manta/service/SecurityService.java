package com.espendwise.manta.service;

import com.espendwise.manta.auth.*;
import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("userAuthenticationService")
public class SecurityService extends DataAccessService implements UserAuthenticationService {

    private static final Logger logger = Logger.getLogger(SecurityService.class);
    //prefix that config uses for any security role
    private static final String SPRING_ROLE_PREFIX = "ROLE_";

    @Autowired
    private MainDbService mainDbService;


    /**
     * Returns a representation of the espendwise userEntity as a Spring UserDetails
     * for authentication.
     *
     * @return populated UserDetails
     */
    @Transactional(readOnly = true)
    public AuthUserDetails loadUserByUsername(@AppDS String datasource, String username) throws DataAccessException {

        logger.info("loadUserByUsername()=> username: " + username);

        UserDAO userDao = new UserDAOImpl(getEntityManager(datasource));

        UserData userData =  userDao.findByUserName(username, false);
        if (userData == null) {
            logger.debug("Username not found");
            throw new UsernameNotFoundException("user not found");
        }

        return createAuthUser(userData, true);

    }

    @Override
    public Pair<AuthUserDetails, AuthUserAccessTokenProperty> loadUserByAccessToken(String datasource, String accessToken) {

        logger.info("loadUserByAccessToken()=> BEGIN" +
                ", accessToken: " + accessToken +
                ", datasource: " + datasource
        );

        Date curDate = new Date();

        PropertyDAO propertyDao = new PropertyDAOImpl(getEntityManager(datasource));
        UserDAO userDao = new UserDAOImpl(getEntityManager(datasource));


        List<PropertyData> accessTokens =  propertyDao.findActiveAccessTokens(accessToken);

        if (accessTokens.size() == 0) {
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.accessTokenNotFound", accessToken);
        }

        if (accessTokens.size() > 1) {
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.multipleAccessTokens", accessToken);
        }

        PropertyData accessTokenData = accessTokens.get(0);

        AuthUserAccessTokenProperty token = new AuthUserAccessTokenProperty(
                datasource,
                accessTokenData.getUserId(),
                accessTokenData.getValue(),
                accessTokenData.getBusEntityId(),
                accessTokenData.getOriginalUserId(),
                accessTokenData.getAddDate(),
                accessTokenData.getAddBy(),
                accessTokenData.getModDate(),
                accessTokenData.getModBy()
        );

        long timePassed = curDate.getTime() - token.getModDate().getTime();

        logger.info("loadUserByAccessToken()=> " + curDate.getTime() + "-" + token.getModDate() + "=" + timePassed);

        String maxAge = AppResourceHolder.getAppResource().getApplicationSettings().getSettings(Constants.APPLICATION_SETTINGS.ACCESS_TOKEN_MAX_AGE);
        if (timePassed > Parse.parseInt(maxAge)) {
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.expired", accessToken);
        }

        UserData user = userDao.findByUserId(token.getUserId());
        if(user == null){
            throw new AuthenticationAccessTokenException("exception.authentication.accessToken.userNotFound", accessToken);
        }

       return new Pair<AuthUserDetails, AuthUserAccessTokenProperty>(
               createAuthUser(user, false),
               token
       );

    }

    private AuthUserDetails createAuthUser(UserData user, boolean grantedAuthority) {

        Long userId = user.getUserId();

        String password = user.getPassword();
        String status =  user.getUserStatusCd();
        String userType =  user.getUserTypeCd();
        String locale =  user.getPrefLocaleCd();
        String username = user.getUserName();

        boolean enabled = ("ACTIVE".equals(status));
        boolean credentialsNonExpired = ("ACTIVE".equals(status));
        boolean accountNonLocked = ("ACTIVE".equals(status));

        //boolean accountNonExpired = ("ACTIVE".equals(status));
        boolean accountNonExpired = true;
        Date now = Utility.setToMidnight(new Date());
        if (Utility.isSet(user.getEffDate())) {
            if (now.before(user.getEffDate())) {
                accountNonExpired = false;
            }
        }
        if (Utility.isSet(user.getExpDate())) {
            if (now.after(user.getExpDate())) {
                accountNonExpired = false;
            }
        }
        
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (grantedAuthority) {
            authorities.add(new GrantedAuthorityImpl(translateUserTypeCodes(userType)));
        }

        logger.info("loadUserByUsername()=> authorities: " + authorities);

        return new AuthUserDetails(
                userId,
                userType,
                Utility.parseLocaleCode(locale),
                authorities,
                password,
                username,
                accountNonExpired,
                enabled,
                credentialsNonExpired,
                accountNonLocked
        );
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<String,AuthDatabaseAccessUnit> findUserDataAccessUnits(String username) {
        return mainDbService.getUserDataAccessUnits(username);
    }

    @Override
    public AuthDatabaseAccessUnit findDataAccessUnit(String datasource) {
        return mainDbService.createDataAccessUnit(datasource);
    }


    @Override
    @Transactional(readOnly = true, noRollbackFor = Throwable.class)
    public List<StoreListEntity> findUserStores(@AppDS String datasource, AuthUserDetails user, List<Long> stores) {
        StoreListDAO sroreDAO = new StoreListDAOImpl(getEntityManager(datasource));
        return RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(user.getUserTypeCd())
                ? sroreDAO.find(stores != null ? stores.toArray(new Long[stores.size()]) : null)
                : sroreDAO.find(user.getUserId(), stores, (Integer) null);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Throwable.class)
    public List<StoreListEntity> findUserStores(@AppDS String datasource, AuthUserDetails user) {
        StoreListDAO sroreDAO = new StoreListDAOImpl(getEntityManager(datasource));
        return RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(user.getUserTypeCd())
                ? sroreDAO.find()
                : sroreDAO.find(user.getUserId(), (Integer) null);
    }

    /**
     * Translates our existing user type code to a config role for security.
     * Mainly this consists of putting the text "ROLE_" in front and replacing
     * spaces with underscores ("_"); although this is done explicitly and not
     * programatically.
     *
     * @param userTypeCd the eSpendwise user role
     * @return the translates config role
     */
    private static String translateUserTypeCodes(String userTypeCd) {
        return SPRING_ROLE_PREFIX + Utility.strNN(userTypeCd).replaceAll(Constants.SPACE, Constants.CHARS.UNDERLINE);
    }
}
