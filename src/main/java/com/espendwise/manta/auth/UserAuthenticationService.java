package com.espendwise.manta.auth;

import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.Pair;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface UserAuthenticationService {

    public AuthUserDetails loadUserByUsername(String datasource, String username) throws DataAccessException;

    public Map<String, AuthDatabaseAccessUnit> findUserDataAccessUnits(String username);

    public List<StoreListEntity> findUserStores(@AppDS String datasource, AuthUserDetails user, List<Long> stores);

    public List<StoreListEntity> findUserStores(@AppDS String datasource, AuthUserDetails user);

    public AuthDatabaseAccessUnit findDataAccessUnit(String datasource);

    public Pair<AuthUserDetails, AuthUserAccessTokenProperty> loadUserByAccessToken(String datasource, String accessToken);
}
