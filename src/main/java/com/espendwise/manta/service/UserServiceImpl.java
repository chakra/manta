package com.espendwise.manta.service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.GroupDAO;
import com.espendwise.manta.dao.GroupDAOImpl;
import com.espendwise.manta.dao.PropertyDAO;
import com.espendwise.manta.dao.PropertyDAOImpl;
import com.espendwise.manta.dao.UserDAO;
import com.espendwise.manta.dao.UserDAOImpl;
import com.espendwise.manta.loader.UserLoader;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserGroupInformationView;
import com.espendwise.manta.model.view.UserHeaderView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.UserEmailNotificationTypeCodes;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationIllegalArgumentException;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.util.validation.rules.UserAccountConfigurationConstraint;
import com.espendwise.manta.util.validation.rules.UserGroupConfigurationConstraint;
import com.espendwise.manta.util.validation.rules.UserNotificationConfigurationConstraint;
import com.espendwise.manta.web.util.SuccessActionMessage;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl extends DataAccessService implements UserService {
	
    @Autowired
	private UserDAO userDao;
    @Autowired
	private GroupDAO groupDao;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserDAO userDao) {
    	this.groupDao = null;
    	this.userDao = userDao;
    }

    public UserServiceImpl(GroupDAO groupDao) {
    	this.groupDao = groupDao;
    	this.userDao = null;
    }

    public UserServiceImpl(GroupDAO groupDao, UserDAO userDao) {
    	this.groupDao = groupDao;
    	this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserData findByUserName(String username, boolean b) {
        UserDAO userDao =  new UserDAOImpl(getEntityManager());
        return userDao.findByUserName(username, b);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserHeaderView findUserHeader(Long userId) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findUserHeader(userId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserListView> findUsersByCriteria(UserListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);
        
        return userDao.findUsersByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserIdentView findUserToEdit(Long storeId, Long userId) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findUserToEdit(storeId, userId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserData findByUserId(Long userId) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Set<String> getAllUserPresentDS(Long userId) {

        Set<String> userDs = new HashSet<String>();
        
        if (userId != null && userId > 0) {

            String[] availableDs = DatabaseAccess.availableUnits();

            EntityManager entityManagerForDsName;
            UserDAO userDao;

            for (String availableD : availableDs) {

                if (isAliveUnit(availableD)) {

                    entityManagerForDsName = getEntityManager(availableD);
                    userDao = new UserDAOImpl(entityManagerForDsName);

                    UserData user = userDao.findByUserId(userId);
                    if (user != null) {
                        userDs.add(availableD);
                    }
                }
            }
        }
        
        return userDs;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserIdentView createUserIdent(UserIdentView userIdent) {
        userDao.setEntityManager(getEntityManager());
        return userDao.createUserIdent(userIdent);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserIdentView modifyUserIdent(UserIdentView userIdent) {
        userDao.setEntityManager(getEntityManager());
        return userDao.modifyUserIdent(userIdent);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserIdentView saveUserIdent( UserIdentView userIdent, Boolean allowCreate) {
       
        EntityManager entityManager = getEntityManager();
        
        UserDAO userDao = new UserDAOImpl(entityManager);
        
        return userDao.saveUserIdentToDs(userIdent, allowCreate);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserIdentView saveUserIdentToDs(@AppDS String dsName, UserIdentView userIdent, Boolean allowCreate) {
       
        EntityManager entityManagerForDs = getEntityManager(dsName);
        
        UserDAO userDao = new UserDAOImpl(entityManagerForDs);
        
        return userDao.saveUserIdentToDs(userIdent, allowCreate);
    }
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findUserAccounts(StoreAccountCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findUserAccounts(criteria);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureUserAccounts(Long userId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected, Boolean removeSiteAssocToo) {

    	if (!deselected.isEmpty() && !removeSiteAssocToo){
	    	ServiceLayerValidation validation = new ServiceLayerValidation();
	        validation.addRule(new UserAccountConfigurationConstraint(storeId, userId, Utility.toIds(deselected)));
	        validation.validate();
    	}
    	
        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureUserAccounts(userId, storeId, selected, deselected, removeSiteAssocToo);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureAllUserAccounts(Long userId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureAllUserAccounts(userId, storeId, associateAllAccounts, associateAllSites);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureAllUserSites(Long userId, Long storeId, List<BusEntityData> selected) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureUserSites(userId, storeId, selected, null);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureUserSitesList(Long userId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureUserSitesList(userId, storeId, selected, deselected);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteListView> findUserSitesByCriteria(SiteListViewCriteria criteria) {
        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findUserSitesByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<PropertyData> findUserProperty(Long userId, String propertyType) {
        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findUserProperty(userId, propertyType);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<Long, GroupData> findGroupsUserIsMemberOf(Long userId, String userType) {

        EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.findAssociatedUserGroups(userId, userType);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserGroupInformationView> findUserGroupInformation(Long userId, String userTypeCd) {

        EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.findUserGroupInformation(userId, userTypeCd);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<String> findManagableUserTypesFor(Long userId) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        return userDao.findManagableUserTypesFor(userId);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureUserGroups(Long userId, Long storeId, UpdateRequest<Long> updateRequest) {

        logger.info("configureUserGroups()=> BEGIN");

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new UserGroupConfigurationConstraint(storeId, userId, updateRequest));

        validation.validate();

        logger.info("configureUserGroups()=> updateRequest:" + updateRequest);

        groupDao.setEntityManager(getEntityManager());

        groupDao.configureUserGroups(
                userId,
                updateRequest.getToCreate(),
                updateRequest.getToDelete()
        );


        logger.info("configureUserGroups()=> END.");

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<PropertyData> findEmailNotificationProperties(UserData userData) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.findUserProperties(
                userData.getUserId(),
                Utility.typeCodes(UserEmailNotificationTypeCodes.values())
        );

    }

    @Override
    public void configureUserNotifications(Long storeId, UserData user, List<PropertyData> emailProperties) {

        logger.info("configureUserNotifications()=> BEGIN");

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new UserNotificationConfigurationConstraint(storeId, user, emailProperties));

        validation.validate();

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureUserGroups(
                user,
                emailProperties
        );

        logger.info("configureUserNotifications()=> END.");
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureUserProperty(Long userId, String propertyType, String propertyValue) {

        EntityManager entityManager = getEntityManager();
        UserDAO userDao = new UserDAOImpl(entityManager);

        userDao.configureUserProperty(userId, propertyType, propertyValue);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long findUserDefaultStore(Long userId) {

        EntityManager entityManager = getEntityManager();

        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        String defStore =
                PropertyUtil.toFirstValueNN(
                        propertyDao.findUserProperties(userId, Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE))
                );


        return Utility.isSet(defStore) ? Parse.parseLong(defStore) : null;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public SuccessActionMessage processUserUpload(Locale locale, Long currStoreId, InputStream inputStream,String streamType){
    	EntityManager entityManager = getEntityManager();
    	UserLoader loader = new UserLoader(entityManager, locale, currStoreId, inputStream, streamType);
    	loader.translate();
    	ValidationRuleResult result = loader.getValidationResult();
        if (result == null) {
            throw new ApplicationIllegalArgumentException(ExceptionReason.SystemReason.ILLEGAL_VALIDATION_RESULT);
        }

        if (result.isFailed()) {
            List<ApplicationExceptionCode> codes = result.getCodes();
            
            ApplicationExceptionCode[] codesArray = codes.toArray(new ApplicationExceptionCode[codes.size()]);
            throw new ValidationException(codesArray);
        }

    	return new SuccessActionMessage("admin.successMessage.fileProcessedWithCount", 
    			new NumberArgument[]{new NumberArgument(loader.getAddedCount()),new NumberArgument(loader.getModifiedCount()),new NumberArgument(loader.getDeletedCount())});
    }



}
