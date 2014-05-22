package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.data.UserStoreData;
import com.espendwise.manta.model.view.AllUserIdentView;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AllUserDAOImpl  extends DAOImpl implements AllUserDAO{

    private static final Logger logger = Logger.getLogger(AllUserDAOImpl.class);

    public AllUserDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public AllUserData findById(Long allUserId) {
        Query q = em.createQuery("SELECT u from  AllUserData u where u.allUserId = (:allUserId)");
        q.setParameter("allUserId", allUserId);
        List r = q.getResultList();
        return r.isEmpty() ? null : (AllUserData) r.get(0);
    }

    @Override
    public AllUserData findByUserName(String username) {
        Query q = em.createQuery("SELECT u from  AllUserData u where u.userName = (:username)");
        q.setParameter("username", username);
        List r = q.getResultList();
        return r.isEmpty() ? null : (AllUserData) r.get(0);
    }
    
    @Override
    public AllUserIdentView findViewByName(String userName) {

        Query q = em.createQuery("Select object(allUser) from AllUserData allUser" +
                                 " where allUser.userName = (:userName)");
        
        q.setParameter("userName", userName);

        List<AllUserData> users = (List<AllUserData>) q.getResultList();

        if (Utility.isSet(users)) {
            return pickupAllUserIdentView(users.get(0));
        } else {
            return null;
        }
        
    }
    
    private AllUserIdentView pickupAllUserIdentView(AllUserData allUserData) {
        if ( allUserData == null || !(Utility.longNN(allUserData.getAllUserId()) > 0)) {
            return null;
        }
        
        AllUserIdentView allUserIdentView = new AllUserIdentView();
        allUserIdentView.setAllUserData(allUserData);
 
        List<UserStoreData> assocs = findAllUserStoreAssocs(allUserData.getAllUserId());
        allUserIdentView.setUserStoreAssoc(assocs);

        return allUserIdentView;
    }
    
    private List<UserStoreData> findAllUserStoreAssocs(Long allUserId) {
        Query q = em.createQuery("Select object(userStore) from UserStoreData userStore" +
                " where userStore.allUserId =(:allUserId)" + 
                " order by userStore.userStoreId");
        
        q.setParameter("allUserId", allUserId);

        List<UserStoreData> userStoreAssocs = (List<UserStoreData>) q.getResultList();
        
        return userStoreAssocs;
    }
    
    public AllUserIdentView saveUserIdentToMain(AllUserIdentView allUserIdent) {
        AllUserData allUserData = allUserIdent.getAllUserData();
        
        if (allUserData.getAllUserId() == null) {
        
            allUserData = super.create(allUserData);
        
        } else {
        
            allUserData = super.update(allUserData);
        
        }
        allUserIdent.setAllUserData(allUserData);
        
        // ==================== Store assocs ===========================
        List<UserStoreData> newStoreAssocs = allUserIdent.getUserStoreAssoc();
        
        List<UserStoreData> oldStoreAssocs = findAllUserStoreAssocs(allUserData.getAllUserId());
        
        Map<Long, UserStoreData> oldMap = new HashMap<Long, UserStoreData>();

        if (Utility.isSet(oldStoreAssocs)) {
            for (UserStoreData el : oldStoreAssocs) {
                oldMap.put(el.getAllStoreId(), el);
            }
        }

        if (Utility.isSet(newStoreAssocs)) {
            for (UserStoreData el : newStoreAssocs) {
                if (oldMap.containsKey(el.getAllStoreId())) {
                    oldMap.remove(el.getAllStoreId());
                } else {
                    el.setAllUserId(allUserData.getAllUserId());
                    super.create(el);
                }
            }
        }
        
        if (oldMap.size() > 0) {
            // remove unused old records
            Iterator<UserStoreData> it = oldMap.values().iterator();
            while (it.hasNext()) {
                em.remove(it.next());
            }
        }
        
        return allUserIdent;
    }

}
