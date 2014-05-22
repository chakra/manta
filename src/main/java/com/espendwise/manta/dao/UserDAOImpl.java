package com.espendwise.manta.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.PasswordHistoryData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserHeaderView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SystemError;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.util.parser.Parse;

/**
 * Sample User DAO class for fetching users with no multi database support.
 */
@Repository
public class UserDAOImpl extends DAOImpl/*<UserData>*/ implements UserDAO {

    protected final Logger logger = Logger.getLogger(getClass());

    public UserDAOImpl() {
        this(null);
    }

    public UserDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public UserData findByUserName(String userName, boolean activeOnly) {

        if (userName == null) {
            return null;
        }

        Query q;
        if (activeOnly) {
            q = em.createQuery(
                    "select object(u) from UserData u where u.userStatusCd = 'ACTIVE' and u.userName=:userName")
                    .setParameter("userName", userName);
        } else {
            q = em.createQuery(
                    "select object(u) from UserData u where u.userName=:userName")
                    .setParameter("userName", userName);
        }

        logger.debug("Executing SQL!");

        List<UserData> users = (List<UserData>) q.getResultList();

        if (users.size() > 1) {
            throw SystemError.multipleUsers(userName);
        }

        return users.size() == 0 ? null : users.get(0);
    }

    public UserData findByUserId(Long userId) {
        return em.find(UserData.class, userId);
    }

    @Override
    public UserHeaderView findUserHeader(Long userId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.UserHeaderView(user.userId, user.userName)" +
                " from UserData user where user.userId = (:userId) "
        );

        q.setParameter("userId", userId);

        List x = q.getResultList();

        return !x.isEmpty() ? (UserHeaderView) x.get(0) : null;
    }

    public List<UserData> findUserDatas() {
        Query q = em.createQuery("select object(u) from UserData u ");
        return (List<UserData>) q.getResultList();
    }
    
    @Override
    public List<UserListView> findUsersByCriteria(UserListViewCriteria criteria) {

        StringBuilder baseQuery = new StringBuilder("Select " +
                "new com.espendwise.manta.model.view.UserListView(" +
                "   user.userId," +
                "   user.userName, " +
                "   user.firstName," +
                "   user.lastName," +
                "   user.userRoleCd," +
                "   user.userTypeCd," +
                "   user.userStatusCd," +
                "   user.firstName||' '||user.lastName||' '||user.userTypeCd" +
                ") " +
                " from com.espendwise.manta.model.fullentity.UserFullEntity user " +
                " left join user.emails email with email.emailTypeCd = (:emailTypeProperty)"
        );
        
           baseQuery.append(" where user.userId in (Select userId from UserAssocData where busEntityId = (:storeId) and userAssocCd = (:storeUserAssocCd))");
        
        if (Utility.isSet(criteria.getAccountFilter())) {
            baseQuery.append(" and user.userId in (Select userId from UserAssocData where busEntityId in (:accountIds) and userAssocCd = (:accountUserAssocCd))");
        }

        if (Utility.isSet(criteria.getSiteId())) {
            baseQuery.append(" and user.userId in (Select userId from UserAssocData where busEntityId = (:siteId) and userAssocCd = (:siteUserAssocCd))");
        }

        if (Utility.isSet(criteria.getUserId())) {
            baseQuery.append(" and user.userId = ").append(Parse.parseLong(criteria.getUserId()));
        }
        
        if (Utility.isSet(criteria.getUserName())) {
            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getUserNameFilterType())) {
                baseQuery.append(" and UPPER(user.userName) like '")
                        .append(QueryHelp.startWith(criteria.getUserName().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getUserNameFilterType())) {
                baseQuery.append(" and UPPER(user.userName) like '")
                        .append(QueryHelp.contains(criteria.getUserName().toUpperCase()))
                        .append("'");
            }
        }

        if (Utility.isSet(criteria.getFirstName())) {
            baseQuery.append(" and UPPER(user.firstName) like '%")
                    .append(QueryHelp.contains(criteria.getFirstName().toUpperCase()))
                    .append("'");
        }
        
        if (Utility.isSet(criteria.getLastName())) {
            baseQuery.append(" and UPPER(user.lastName) like '%")
                    .append(QueryHelp.contains(criteria.getLastName().toUpperCase()))
                    .append("'");
        }
        
        if (Utility.isSet(criteria.getUserType())) {
            baseQuery.append(" and UPPER(user.userTypeCd) = '")
                     .append(QueryHelp.escapeQuotes(criteria.getUserType().toUpperCase()))
                     .append("'");
        }

        if (criteria.getAllowedUserTypes() != null) {
            if (criteria.getAllowedUserTypes().isEmpty()) {
                baseQuery.append(" and 1 = 2");
            } else {
                baseQuery.append(" and UPPER(user.userTypeCd) in (")
                        .append(QueryHelp.in(criteria.getAllowedUserTypes()))
                        .append(")");
            }
        }
        
        if (Utility.isSet(criteria.getEmail())) {
            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getEmailFilterType())) {
                baseQuery.append(" and UPPER(email.emailAddress) like '")
                        .append(QueryHelp.startWith(criteria.getEmail().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getEmailFilterType())) {
                baseQuery.append(" and UPPER(email.emailAddress) like '")
                        .append(QueryHelp.contains(criteria.getEmail().toUpperCase()))
                        .append("'");
            }
        }
        
        if (Utility.isSet(criteria.getRole())) {
            if (RefCodeNames.USER_ROLE_CD.SERVICE_VENDOR.equals(criteria.getRole())) {
                baseQuery.append(" and user.userRoleCd like '")
                         .append(QueryHelp.contains(Constants.USER_ROLE.SERVICE_VENDOR_ROLE))
                         .append("'");
            } else if (RefCodeNames.USER_ROLE_CD.SITE_MANAGER.equals(criteria.getRole())) {
                baseQuery.append(" and user.userRoleCd like '")
                         .append(QueryHelp.contains(Constants.USER_ROLE.SITE_MANAGER_ROLE))
                         .append("'");
            } else {
                baseQuery.append(" and user.userRoleCd not like '")
                         .append(QueryHelp.contains(Constants.USER_ROLE.SERVICE_VENDOR_ROLE))
                         .append("'")
                         .append(" and user.userRoleCd not like '")
                         .append(QueryHelp.contains(Constants.USER_ROLE.SITE_MANAGER_ROLE))
                         .append("'");
            }
        }
        
        if (Utility.isSet(criteria.getLanguage())) {
            baseQuery.append(" and UPPER(user.prefLocaleCd) like '")
                     .append(QueryHelp.startWith(criteria.getLanguage().toUpperCase()))
                     .append("'");
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and user.userStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.USER_STATUS_CD.INACTIVE));
        }

        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("emailTypeProperty", RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("storeUserAssocCd", RefCodeNames.USER_ASSOC_CD.STORE);

        if (Utility.isSet(criteria.getAccountFilter())) {
            q.setParameter("accountIds", criteria.getAccountFilter());
            q.setParameter("accountUserAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT);
        }
        
        if (Utility.isSet(criteria.getSiteId())) {
            q.setParameter("siteId", criteria.getSiteId());
            q.setParameter("siteUserAssocCd", RefCodeNames.USER_ASSOC_CD.SITE);
        }

        if (criteria.getLimit() != null) {  q.setMaxResults(criteria.getLimit());  }

        List<UserListView> resultList = q.getResultList();

        return resultList;
    }

    @Override
    public List<BusEntityData> findUserAccounts(StoreAccountCriteria criteria) {
        if (criteria.getUserId() == null || criteria.getStoreId() == null) {
            return new ArrayList<BusEntityData>();
        }
        
        StringBuilder baseQuery = new StringBuilder(
                "Select object(account) from BusEntityData account, BusEntityAssocData accountToStore, UserAssocData userToAccount" +
                " where userToAccount.userId = (:userId)" +
                " and userToAccount.userAssocCd =(:userToAccountAssocCd)" +
                " and account.busEntityId = accountToStore.busEntity1Id" +
                " and accountToStore.busEntityAssocCd = (:accountToStoreAssocCd)" +
                " and accountToStore.busEntity2Id = (:storeId)" +
                " and userToAccount.busEntityId = accountToStore.busEntity1Id"
        );
        
        if (Utility.isSet(criteria.getAccountId())) {
            baseQuery.append(" and account.busEntityId = ").append(criteria.getAccountId());
        }
        
        if (Utility.isSet(criteria.getName())) {
            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {
                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {
                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                        .append("'");
            }
        }
        
        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and account.busEntityStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }
        
        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("userId", criteria.getUserId());
        q.setParameter("userToAccountAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT);
        q.setParameter("accountToStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);

        if (criteria.getLimit() != null) { q.setMaxResults(criteria.getLimit()); }

        List<BusEntityData> resultList = q.getResultList();
        
        return resultList;
    }

    @Override
    public UserIdentView findUserToEdit(Long storeId, Long userId) {

        Query q = em.createQuery("Select object(user) from UserData user, UserAssocData userToStore" +
                " where user.userId = (:userId)" +
                " and user.userId = userToStore.userId" +
                " and userToStore.busEntityId = (:storeId)" +
                " and userToStore.userAssocCd =(:userToStoreAssocCd)");
        
        q.setParameter("userId", userId);
        q.setParameter("storeId", storeId);
        q.setParameter("userToStoreAssocCd", RefCodeNames.USER_ASSOC_CD.STORE);

        List<UserData> users = (List<UserData>) q.getResultList();
        
        if (Utility.isSet(users)) {
            return pickupUserIdentView(users.get(0));
        } else {
            return null;
        }
    }
    
    private UserIdentView pickupUserIdentView(UserData userData) {

        if ( userData == null || !(Utility.longNN(userData.getUserId()) > 0)) {
            return null;
        }        
        
        UserIdentView userIdentView = new UserIdentView();
        userIdentView.setUserData(userData);

        List<EmailData> emails = findUserEmails(userData.getUserId());
        if (Utility.isSet(emails)) {
            for (EmailData email : emails) {
                if (RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT.equals(email.getEmailTypeCd())) {
                    if (userIdentView.getEmailData() == null) {
                        userIdentView.setEmailData(email);
                    }
                } else if (RefCodeNames.EMAIL_TYPE_CD.ESCALATION.equals(email.getEmailTypeCd())) {
                    if (userIdentView.getEscalationEmailData() == null) {
                        userIdentView.setEscalationEmailData(email);
                    }
                } else if (RefCodeNames.EMAIL_TYPE_CD.SMS.equals(email.getEmailTypeCd())) {
                    if (userIdentView.getSmsEmailData() == null) {
                        userIdentView.setSmsEmailData(email);
                    }
                }
            }
        }
        List<PhoneData> phones = findUserPhones(userData.getUserId());
        if (Utility.isSet(phones)) {
            for (PhoneData phone : phones) {
                if (RefCodeNames.PHONE_TYPE_CD.PHONE.equals(phone.getPhoneTypeCd())) {
                    if (userIdentView.getPhoneData() == null) {
                        userIdentView.setPhoneData(phone);
                    }
                } else if (RefCodeNames.PHONE_TYPE_CD.FAX.equals(phone.getPhoneTypeCd())) {
                    if (userIdentView.getFaxPhoneData() == null) {
                        userIdentView.setFaxPhoneData(phone);
                    }
                } else if (RefCodeNames.PHONE_TYPE_CD.MOBILE.equals(phone.getPhoneTypeCd())) {
                    if (userIdentView.getMobilePhoneData() == null) {
                        userIdentView.setMobilePhoneData(phone);
                    }
                }
            }
        }
        List<AddressData> addresses = findUserAddresses(userData.getUserId());
        if (Utility.isSet(addresses)) {
            userIdentView.setAddressData(addresses.get(0));
        }
        List<UserAssocData> assocs = findUserAssocs(userData.getUserId());
            userIdentView.setUserAssocs(assocs);
        List<PropertyData> properties = findUserProperties(userData.getUserId());
            userIdentView.setUserProperties(properties);
            
        List<GroupAssocData> userGroupAssocs = findUserGroupAssocs(userData.getUserId());
        userIdentView.setUserGroupAssocs(userGroupAssocs);

        return userIdentView;
    }
    
    private List<EmailData> findUserEmails(Long userId) {
        Query q = em.createQuery("Select object(email) from EmailData email" +
                " where email.emailStatusCd = (:emailStatusCd)" +
                " and email.userId =(:userId)" + 
                " order by email.emailId");
        
        q.setParameter("userId", userId);
        q.setParameter("emailStatusCd", RefCodeNames.EMAIL_STATUS_CD.ACTIVE);

        List<EmailData> emails = (List<EmailData>) q.getResultList();
        
        return emails;
    }
    
    private List<PhoneData> findUserPhones(Long userId) {
        Query q = em.createQuery("Select object(phone) from PhoneData phone" +
                " where phone.phoneStatusCd = (:phoneStatusCd)" +
                " and phone.userId =(:userId)" + 
                " order by phone.phoneId");
        
        q.setParameter("userId", userId);
        q.setParameter("phoneStatusCd", RefCodeNames.PHONE_STATUS_CD.ACTIVE);

        List<PhoneData> phones = (List<PhoneData>) q.getResultList();
        
        return phones;
    }
    
    private List<AddressData> findUserAddresses(Long userId) {
        Query q = em.createQuery("Select object(address) from AddressData address" +
                " where address.addressStatusCd = (:addressStatusCd)" +
                " and address.userId =(:userId)" + 
                " and address.addressTypeCd =(:addressTypeCd)" + 
                " order by address.addressId");
        
        q.setParameter("userId", userId);
        q.setParameter("addressTypeCd", RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("addressStatusCd", RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);

        List<AddressData> addresses = (List<AddressData>) q.getResultList();
        
        return addresses;
    }
    
    @Override
    public List<UserAssocData> findUserAssocs(Long userId) {
        Query q = em.createQuery("Select object(assoc) from UserAssocData assoc" +
                " where assoc.userId =(:userId)" + 
                " order by assoc.userAssocId");
        
        q.setParameter("userId", userId);

        List<UserAssocData> assocs = (List<UserAssocData>) q.getResultList();
        
        return assocs;
    }
     
    private List<PropertyData> findUserProperties(Long userId) {
        Query q = em.createQuery("Select object(property) from PropertyData property" +
                " where property.userId =(:userId)" + 
                " order by property.propertyId");
        
        q.setParameter("userId", userId);
        //q.setParameter("propertyStatusCd", RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);

        List<PropertyData> properties = (List<PropertyData>) q.getResultList();
        
        return properties;
    }
    
    @Override
    public List<PropertyData> findUserProperty(Long userId, String propertyType) {
        Query q = em.createQuery("Select object(property) from PropertyData property" +
                " where property.userId =(:userId)" +
                " and property.propertyTypeCd =(:propertyTypeCd)" +
                " and property.propertyStatusCd =(:propertyStatusCd)" +
                " order by property.propertyId");
        
        q.setParameter("userId", userId);
        q.setParameter("propertyStatusCd", RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
        q.setParameter("propertyTypeCd", propertyType);

        List<PropertyData> properties = (List<PropertyData>) q.getResultList();
        
        return properties;
    }
    
    
    public List<GroupAssocData> findUserGroupAssocs(Long userId) {
        Query q = em.createQuery("Select object(assoc) from GroupAssocData assoc" +
                " where assoc.userId =(:userId)");
        
        q.setParameter("userId", userId);

        List<GroupAssocData> assocs = (List<GroupAssocData>) q.getResultList();
        
        return assocs;
    }

    public void delete(UserData user) {
        if (user.getUserId() != null) {
            em.remove(user);
        }
    }

    @Override
    public List<String> findManagableUserTypesFor(Long userId) {

        Query q = em.createQuery("Select user.userTypeCd  from UserData user where user.userId =(:userId)");

        q.setParameter("userId", userId);

        List<String> x = q.getResultList();

        if(!x.isEmpty()){

            String type = x.get(0);

            return AppResourceHolder
                    .getAppResource()
                    .getDbConstantsResource()
                    .getManagableUserTypes(type);
        }

        return Utility.emptyList(String.class);
    }


    public UserIdentView saveUserIdentToDs(UserIdentView userIdent, Boolean allowCreate) {
        UserData userData = userIdent.getUserData();
        
        if (userData.getUserId() == null && !allowCreate) {
            return userIdent;
        }
        
        if (userData.getUserId() == null) {
            return createUserIdent(userIdent);
        } else {
            return modifyUserIdent(userIdent);
        }
    }
    
    @Override
    public UserIdentView createUserIdent(UserIdentView userIdent) {
        UserData userData = userIdent.getUserData();
        userData = super.create(userData);
        userIdent.setUserData(userData);
        userIdent = createOrUpdateUserContact(userIdent);
        userIdent = createOrUpdateUserIdentProperties(userIdent);
        userIdent = createOrUpdateUserAssocs(userIdent);
        userIdent = createOrUpdateUserGroupAssocs(userIdent);
        userIdent = createPasswordHistoryIfChanged(userIdent);
        return userIdent;
    }
    
    public UserIdentView modifyUserIdent(UserIdentView userIdent) {
        UserData userData = userIdent.getUserData();
        userData = super.update(userData);
        userIdent.setUserData(userData);
        userIdent = createOrUpdateUserContact(userIdent);
        userIdent = createOrUpdateUserIdentProperties(userIdent);
        userIdent = createOrUpdateUserAssocs(userIdent);
        userIdent = createOrUpdateUserGroupAssocs(userIdent);
        userIdent = createPasswordHistoryIfChanged(userIdent);
        return userIdent;
    }
    
    private UserIdentView createOrUpdateUserContact(UserIdentView userIdent) {

        AddressData primaryAddress = userIdent.getAddressData();
        PhoneData phone = userIdent.getPhoneData();
        PhoneData fax = userIdent.getFaxPhoneData();
        PhoneData mobile = userIdent.getMobilePhoneData();
        EmailData email = userIdent.getEmailData();
        EmailData escalationEmail = userIdent.getEscalationEmailData();
        EmailData smsEmail = userIdent.getSmsEmailData();

        AddressDAO addressDao = new AddressDAOImpl(em);
        EmailDAO emailDao = new EmailDAOImpl(em);
        PhoneDAO phoneDao = new PhoneDAOImpl(em);
        
        Long userId = userIdent.getUserData().getUserId();
        primaryAddress = addressDao.updateUserAddress(userId, primaryAddress);
        phone = phoneDao.updateUserPhone(userId, phone);
        fax = phoneDao.updateUserPhone(userId, fax);
        mobile = phoneDao.updateUserPhone(userId, mobile);
        email = emailDao.updateUserEmail(userId, email);
        escalationEmail = emailDao.updateUserEmail(userId, escalationEmail);
        smsEmail = emailDao.updateUserEmail(userId, smsEmail);

        userIdent.setAddressData(primaryAddress);
        userIdent.setPhoneData(phone);
        userIdent.setFaxPhoneData(fax);
        userIdent.setMobilePhoneData(mobile);
        userIdent.setEmailData(email);
        userIdent.setEscalationEmailData(escalationEmail);
        userIdent.setSmsEmailData(smsEmail);
        
        return userIdent;

    }
    
    private UserIdentView createOrUpdateUserIdentProperties(UserIdentView userIdent) {

        List<PropertyData> properties = userIdent.getUserProperties();

        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        properties = propertyDAO.saveUserProperties(userIdent.getUserData().getUserId(), properties);
        
        userIdent.setUserProperties(properties);

        return userIdent;
    }
    
    private UserIdentView createOrUpdateUserAssocs(UserIdentView userIdent) {
        List<UserAssocData> newAssocs = userIdent.getUserAssocs();
        
        UserAssocDAO userAssocDAO = new UserAssocDAOImpl(em);
        newAssocs = userAssocDAO.updateUserAssocs(userIdent.getUserData().getUserId(), newAssocs);
        
        userIdent.setUserAssocs(newAssocs);
        
        return userIdent;
    }
    
    private UserIdentView createOrUpdateUserGroupAssocs(UserIdentView userIdent) {
        List<GroupAssocData> newAssocs = userIdent.getUserGroupAssocs();
        if (newAssocs == null) {
        	newAssocs = new ArrayList<GroupAssocData>();
        }
        
        GroupAssocDAO groupAssocDAO = new GroupAssocDAOImpl(em);
        groupAssocDAO.updateUserGroupAssocs(userIdent.getUserData().getUserId(), newAssocs);
        
        return userIdent;
    }

    @Override
    public void configureUserAccounts(Long userId,
                                      Long storeId,
                                      List<BusEntityData> selected,
                                      List<BusEntityData> deselected,
                                      Boolean removeSiteAssocToo) {
        if (userId != null && storeId != null) {
            UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

            List<UserAssocData> oldAssocList = userAssocDao.readUserAccountAssoc(userId, storeId);
            
            Map<Long, UserAssocData> oldMap = new HashMap<Long, UserAssocData>();
            
            if (Utility.isSet(oldAssocList)) {
                for (UserAssocData el : oldAssocList) {
                    oldMap.put(el.getBusEntityId(), el);
                }
            }
            
            UserAssocData assoc;
            if (Utility.isSet(selected)) {
                for (BusEntityData el : selected) {
                    if (!oldMap.containsKey(el.getBusEntityId())) { // create new assoc
                        assoc = new UserAssocData();
                        assoc.setUserId(userId);
                        assoc.setBusEntityId(el.getBusEntityId());
                        assoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.ACCOUNT);
                        super.create(assoc);
                    }
                }
                
            }
           
            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                for (BusEntityData el : deselected) {
                    assoc = oldMap.get(el.getBusEntityId());
                    if (assoc != null) {
                        if (removeSiteAssocToo) {
                            List<UserAssocData> userSiteAssocs = userAssocDao.readUserSiteAssocsForAccount(userId, assoc.getBusEntityId());
                            if (Utility.isSet(userSiteAssocs)) {
                                for (UserAssocData userSiteAssoc : userSiteAssocs) {
                                    em.remove(userSiteAssoc);
                                }
                            }
                        }
                        em.remove(assoc);
                    }
                }
            }
        }
    }
    
    @Override
    public void configureUserProperty(Long userId, String propertyType, String propertyValue) {
        if (userId != null && Utility.isSet(propertyType)) {

            List<PropertyData> oldProperties = findUserProperty(userId, propertyType);
            
            Map<String, PropertyData> oldMap = new HashMap<String, PropertyData>();
            
            if (Utility.isSet(oldProperties)) {
                for (PropertyData el : oldProperties) {
                    oldMap.put(el.getValue(), el);
                }
            }
            
            List<PropertyData> newProperties = new ArrayList<PropertyData>();
            PropertyData property;
            if (Utility.isSet(propertyValue)) {
                property = new PropertyData();
                property.setUserId(userId);
                property.setValue(propertyValue);
                property.setPropertyStatusCd(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
                property.setPropertyTypeCd(propertyType);
                property.setShortDesc(propertyType);
                newProperties.add(property);
            }

            for (PropertyData el : newProperties) {
                if (oldMap.containsKey(el.getValue())) {
                    oldMap.remove(el.getValue());
                } else { // create new property
                    super.create(el);
                }
            }
            
            if (oldMap.size() > 0) {
                Iterator<PropertyData> it = oldMap.values().iterator();
                while (it.hasNext()) { // remove unused old properties
                    property = it.next();
                    em.remove(property);
                }
            }
        }
    }

    @Override
    public void configureUserGroups(UserData user, List<PropertyData> emailProperties) {
        if(user!=null && Utility.longNN(user.getUserId())> 0){
            update(user);
            PropertyDAO propertyDAO = new PropertyDAOImpl(em);
            propertyDAO.updateUserProperties(user.getUserId(), emailProperties);
        }
    }

    @Override
    public void configureAllUserAccounts(Long userId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites) {
        AccountDAO accountDao = new AccountDAOImpl(em);
        
        if (userId != null && storeId != null) {
            if (associateAllAccounts) {
                StoreAccountCriteria criteria = new StoreAccountCriteria();
                criteria.setStoreId(storeId);
                criteria.setActiveOnly(true);

                List<BusEntityData> storeAccounts = accountDao.findAccounts(criteria);
                if (Utility.isSet(storeAccounts)) {
                    configureUserAccounts(userId, storeId, storeAccounts, null, false);
                }
            }
            if (associateAllSites) {
                List<BusEntityData> accountsSites = findSitesForConfiguredAccounts(userId, storeId);
                if (Utility.isSet(accountsSites)) {
                    configureUserSites(userId, storeId, accountsSites, null);
                }
            }
        }
                
    }
    
    @Override
    public void configureUserSites(Long userId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected) {
        if (userId != null && storeId != null) {
            UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

            List<UserAssocData> oldAssocList = userAssocDao.readUserSiteAssoc(userId, storeId);
            
            Map<Long, UserAssocData> oldMap = new HashMap<Long, UserAssocData>();
            
            if (Utility.isSet(oldAssocList)) {
                for (UserAssocData el : oldAssocList) {
                    oldMap.put(el.getBusEntityId(), el);
                }
            }
            
            UserAssocData assoc;
            if (Utility.isSet(selected)) {
                for (BusEntityData el : selected) {
                    if (!oldMap.containsKey(el.getBusEntityId())) { // create new assoc
                        assoc = new UserAssocData();
                        assoc.setUserId(userId);
                        assoc.setBusEntityId(el.getBusEntityId());
                        assoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.SITE);
                        super.create(assoc);
                    }
                }
                
            }

            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                for (BusEntityData el : deselected) {
                    assoc = oldMap.get(el.getBusEntityId());
                    if (assoc != null) {
                        em.remove(assoc);
                    }
                }
            }
        }
    }
    
    @Override
    public void configureUserSitesList(Long userId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected) {
        if (userId != null && storeId != null) {
            UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

            List<UserAssocData> oldAssocList = userAssocDao.readUserSiteAssoc(userId, storeId);
            
            Map<Long, UserAssocData> oldMap = new HashMap<Long, UserAssocData>();
            
            if (Utility.isSet(oldAssocList)) {
                for (UserAssocData el : oldAssocList) {
                    oldMap.put(el.getBusEntityId(), el);
                }
            }
            
            UserAssocData assoc;
            if (Utility.isSet(selected)) {
                for (SiteListView el : selected) {
                    if (!oldMap.containsKey(el.getSiteId())) { // create new assoc
                        assoc = new UserAssocData();
                        assoc.setUserId(userId);
                        assoc.setBusEntityId(el.getSiteId());
                        assoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.SITE);
                        super.create(assoc);
                    }
                }
            }

            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                for (SiteListView el : deselected) {
                    assoc = oldMap.get(el.getSiteId());
                    if (assoc != null) {
                        em.remove(assoc);
                    }
                }
            }
        }
    }
    
    public List<BusEntityData> findSitesForConfiguredAccounts(Long userId, Long storeId) {
        List<BusEntityData> sites = new ArrayList<BusEntityData>();
        
        if (userId != null && storeId != null) {
            
            Query q = em.createQuery("Select object(sites) from BusEntityData sites, UserAssocData userToAccount," +
                    " BusEntityAssocData accountToStore, BusEntityAssocData siteToAccount" +
                    " where userToAccount.userId = (:userId)" +
                    " and userToAccount.userAssocCd = (:userToAccountAssocCd)" +
                    " and userToAccount.busEntityId = accountToStore.busEntity1Id" +
                    " and accountToStore.busEntity2Id = (:storeId)" +
                    " and accountToStore.busEntityAssocCd = (:accountToStoreAssocCd)" +
                    " and siteToAccount.busEntity2Id = userToAccount.busEntityId" +
                    " and siteToAccount.busEntityAssocCd = (:siteToAccountAssocCd)" +
                    " and sites.busEntityId = siteToAccount.busEntity1Id" +
                    " and sites.busEntityStatusCd = (:siteStatusCd)"
                    );

            q.setParameter("userId", userId);
            q.setParameter("storeId", storeId);
            q.setParameter("userToAccountAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT);
            q.setParameter("accountToStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
            q.setParameter("siteToAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("siteStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);

            sites = (List<BusEntityData>) q.getResultList();
        }
        
        return sites;
    }

    @Override
    public List<SiteListView> findUserSitesByCriteria(SiteListViewCriteria criteria) {

        List<SiteListView> sites = new ArrayList<SiteListView>();

        logger.info("findUserSitesByCriteria() => BEGIN, criteria : " + criteria);

        if (criteria.getUserId() != null) {
            StringBuilder queryString = new StringBuilder();
            queryString.append("Select distinct new com.espendwise.manta.model.view.SiteListView (" +
                    " site.busEntityId," +
                    " site.shortDesc," +
                    " account.busEntityId," +
                    " account.shortDesc," +
                    " site.busEntityStatusCd," +
                    " address.address1," +
                    " address.address2," +
                    " address.address2," +
                    " address.address4," +
                    " address.countryCd," +
                    " address.countyCd," +
                    " address.city," +
                    " address.stateProvinceCd," +
                    " address.postalCode )" +
                    " from BusEntityFullEntity site " +
                    " left outer join site.addresses address with address.addressTypeCd in (:typeAddressCd)" +
                    " left outer join site.properties referenceNumber with referenceNumber.shortDesc = (:refNumberCode) and referenceNumber.value is not null");
 
            if (criteria.isConfiguredOnly()) {
                queryString.append(" inner join site.userAssocs userSite" +
                                   " inner join userSite.userId user");
            }

            queryString.append(" inner join site.busEntityAssocsForBusEntity1Id siteAccount" +
                               " inner join siteAccount.busEntity2Id account" +
                               " inner join account.busEntityAssocsForBusEntity1Id accountStore");

            if (criteria.isUseUserToAccountAssoc()) {       
                queryString.append(" inner join account.userAssocs userAccount");
                if (!criteria.isConfiguredOnly()) {
                    queryString.append(" inner join userAccount.userId user");
                }
            }

            queryString.append(" where site.busEntityTypeCd = (:typeCdOfSite)" +
                               " and accountStore.busEntity2Id.busEntityId = (:storeId)" +
                               " and accountStore.busEntityAssocCd = (:accountStoreAssocCd)" +
                               " and siteAccount.busEntityAssocCd = (:siteAccountAssocCd)" +
                               " and account.busEntityTypeCd = (:typeCdOfAccount)");
            
            if (criteria.isConfiguredOnly() || criteria.isUseUserToAccountAssoc()) {
                queryString.append(" and user.userId = (:userId)");
            }

            if (criteria.isConfiguredOnly()) {    
                queryString.append(" and userSite.userAssocCd = (:userSiteAssocCd)");
            }
            
            if (criteria.isUseUserToAccountAssoc()) {       
                queryString.append(" and userAccount.userAssocCd = (:userAccountAssocCd)" +
                                   " and userAccount.userId.userId = user.userId");
            }
                    
            queryString.append((Utility.isSet(criteria.getSiteId()) ? " and site.busEntityId = (:siteId)" : "") +
                    (Utility.isSet(criteria.getSiteName()) ? " and Upper(site.shortDesc)  like :siteName" : "") +
                    (Utility.isTrue(criteria.isActiveOnly()) ? " and site.busEntityStatusCd <> (:inactiveStatusCd)" : "") +
                    (Utility.isSet(criteria.getRefNumber()) ? " and Upper(referenceNumber.value) like :referenceNumber" : "") +
                    (Utility.isSet(criteria.getCity()) ? " and Upper(address.city) like :city" : "") +
                    (Utility.isSet(criteria.getState()) ? " and Upper(address.stateProvinceCd) like :state" : "") +
                    (Utility.isSet(criteria.getPostalCode()) ? " and Upper(address.postalCode) like :postalCode" : ""));
            
            String query = queryString.toString();
            Query q = em.createQuery(query);
            
            if (criteria.isConfiguredOnly()) {
                q.setParameter("userSiteAssocCd", RefCodeNames.USER_ASSOC_CD.SITE);
            }
            if (criteria.isUseUserToAccountAssoc()) {
                q.setParameter("userAccountAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT);
            }
            q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
            q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
            q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
            q.setParameter("refNumberCode", RefCodeNames.PROPERTY_TYPE_CD.SITE_REFERENCE_NUMBER);
            q.setParameter("typeAddressCd", Utility.toList(RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_SHIPPING, RefCodeNames.ADDRESS_TYPE_CD.SHIPPING));
            q.setParameter("storeId", criteria.getStoreId());

            if (criteria.isConfiguredOnly() || criteria.isUseUserToAccountAssoc()) {
                q.setParameter("userId", criteria.getUserId());
            }

            if (Utility.isSet(criteria.getSiteId())) {
                q.setParameter("siteId", criteria.getSiteId());
            }

            if (Utility.isSet(criteria.getSiteName())) {
                q.setParameter("siteName",
                        QueryHelp.toFilterValue(
                                criteria.getSiteName().toUpperCase(),
                                criteria.getSiteNameFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getRefNumber())) {
                q.setParameter("referenceNumber",
                        QueryHelp.toFilterValue(
                                criteria.getRefNumber().toUpperCase(),
                                criteria.getRefNumberFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getCity())) {
                q.setParameter("city",
                        QueryHelp.toFilterValue(
                                criteria.getCity().toUpperCase(),
                                criteria.getCityFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getState())) {
                q.setParameter("state",
                        QueryHelp.toFilterValue(
                                criteria.getState().toUpperCase(),
                                criteria.getStateFilterType()
                        )
                );
            }
            
            if (Utility.isSet(criteria.getPostalCode())) {
                q.setParameter("postalCode",
                        QueryHelp.toFilterValue(
                                criteria.getPostalCode().toUpperCase(),
                                criteria.getPostalCodeFilterType()
                        )
                );
            }

            if (Utility.isTrue(criteria.isActiveOnly())) {
                q.setParameter("inactiveStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
            }

            if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
            }

            sites = q.getResultList();

        }

        logger.info("findUserSitesByCriteria() => END, fetched : " + sites.size() + " rows");

        return sites;
    }
    
    // If password changed (current password is different from most recent password from history table)
    // will add a new password history record. Column NEED_INITIAL_RESET flag is set based on
    // user's type and store property RESET_PASSWORD_UPON_INIT_LOGIN, store profile property CHANGE_PASSWORD and 
    // account property CHANGE_PASSWORD
    private UserIdentView createPasswordHistoryIfChanged(UserIdentView userIdent) {
    	// check if password changed
    	Query q = em.createQuery("select object(phd) from PasswordHistoryData phd " +
    			"where phd.passwordHistoryId in (select max(phd2.passwordHistoryId) " +
    			"from PasswordHistoryData phd2 where phd2.userId = :userId)")
    			.setParameter("userId", userIdent.getUserData().getUserId());
    	
    	List<PasswordHistoryData> historyDatas = q.getResultList();
    	PasswordHistoryData phD = null;
    	if (historyDatas.size() > 0){ // if password history exists and last password is same as current, do noting
    		phD = historyDatas.get(0);
    		if (userIdent.getUserData().getPassword().equals(phD.getPassword())){
        		return userIdent;
        	}
    	}
    	
    	Long userId = userIdent.getUserData().getUserId();
    	// get NEED_INITIAL_RESET flag for new password.
    	boolean resetOponInitLogin = false;
    	for (int i = 0; i < userIdent.getUserStoreAssocs().size(); i++){
    		AllStoreIdentificationView userStoreAssoc = userIdent.getUserStoreAssocs().get(i);
    		Long storeId = userStoreAssoc.getStoreId();
    		PropertyDAO propertyDao = new PropertyDAOImpl(em);
    		String propertyValue = propertyDao.getEntityPropertyValue(userStoreAssoc.getStoreId(), RefCodeNames.PROPERTY_TYPE_CD.RESET_PASSWORD_UPON_INIT_LOGIN);
    		if (Utility.isTrue(propertyValue)) {
    			/**
    			 * For MSB user, NEED_INITIAL_RESET will set to true only store property RESET_PASSWORD_UPON_INIT_LOGIN, 
    			 * store profile property CHANGE_PASSWORD,account property CHANGE_PASSWORD are all set to true 
    			 */
    			if (userIdent.getUserData().getUserTypeCd().equals(RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER)){
    				// get user store profile property CHANGE_PASSWORD
        			q = em.createQuery("select storeProfiles.display from StoreProfileData storeProfiles " +
        	    	        " where storeProfiles.storeId = (:storeId)" +
        	    	        " and storeProfiles.shortDesc = (:shortDesc) ")
        	    	        .setParameter("storeId", storeId)
        	    	        .setParameter("shortDesc", RefCodeNames.STORE_PROFILE_FIELD.CHANGE_PASSWORD);
        			List<String> displays = q.getResultList();
        			if (displays.size() > 0 && Utility.isTrue(displays.get(0))) {
    	    			// get user account property ALLOW_USER_CHANGE_PASSWORD    				
        				q = em.createQuery("select distinct ap.value from PropertyData ap " +
            	    	        " where ap.busEntityId in (" +
            	    	        "   select accountBus.busEntityId from BusEntityData accountBus, UserAssocData userToAccount " +
            	    	        "   where accountBus.busEntityId = userToAccount.busEntityId" +
            	    	        "   and userToAccount.userId = :userId" +
            	    	        "   and accountBus.busEntityStatusCd = :busEntityStatus" +
            	    	        "   and userToAccount.userAssocCd = :userAssocCd" +
            	    	        "   )" +
            	    	        " and ap.shortDesc = :propertyTypeCd")
            	    	        .setParameter("userId", userId)
            	    	        .setParameter("busEntityStatus", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE)
            	    	        .setParameter("userAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT)
            	    	        .setParameter("propertyTypeCd", RefCodeNames.PROPERTY_TYPE_CD.ALLOW_USER_CHANGE_PASSWORD);
            	    	        
    	    			List<String> values = q.getResultList();
    	                if (values.size() > 0) {
    	                	resetOponInitLogin = true;
    		                for (String value : values){
    		                	if (!Utility.isTrue(value)){
    		                		resetOponInitLogin = false;
    		                		break;
    		                	}
    		                }
    	                }
    	    		}
        		/**
        		 * For Store Administrator user, only one store property RESET_PASSWORD_UPON_INIT_LOGIN need to be true
        		 */
    			} else if (userIdent.getUserData().getUserTypeCd().equals(RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR)){
    				resetOponInitLogin = true;
    				break;
    			}
    		}
    	}
    	    	
    	// password history
    	phD = new PasswordHistoryData();
    	phD.setUserId(userId);
    	phD.setPassword(userIdent.getUserData().getPassword());
    	if (resetOponInitLogin)
    		phD.setNeedInitialReset(Constants.TRUE);
    	else
    		phD.setNeedInitialReset(Constants.FALSE);
    	super.create(phD);
    	return userIdent;    	
    }
    
    public UserData updateUserData(UserData userData) {
        userData = super.update(userData);
        return userData;
    }

}
