package com.espendwise.manta.util.validation.rules;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.GroupDAO;
import com.espendwise.manta.dao.GroupDAOImpl;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class GroupUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(GroupUpdateConstraint.class);

    private GroupView groupView;

    public GroupUpdateConstraint(GroupView groupView) {
        this.groupView = groupView;
    }

    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");

        ValidationRuleResult result = new ValidationRuleResult();

        checkUnique(result);
        if (result.isFailed()) {
            return result;
        }
        
        checkGroupStoreAssociation(result);
        if (result.isFailed()) {
            return result;
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    private void checkUnique(ValidationRuleResult result) {
    	GroupService groupService = getGroupService();
        GroupSearchCriteria criteria = new GroupSearchCriteria();

        GroupData group = groupView.getGroup();
        criteria.setGroupName(group.getShortDesc());
        criteria.setGroupType(group.getGroupTypeCd());
        
        List<GroupListView> groupList = groupService.findGroupViewsByCriteria(criteria);
        
        //if the group being saved is in the list then remove it, since it should not be considered
        //for the uniqueness test.
        if (Utility.isSet(groupList)) {
        	Iterator<GroupListView> iterator = groupList.iterator();
        	while (iterator.hasNext()) {
        		GroupListView groupV = iterator.next();
        		if (!groupV.getGroupId().equals(group.getGroupId())) {
        			result.failed(ExceptionReason.GroupUpdateReason.GROUP_MUST_BE_UNIQUE,
        					new StringArgument(group.getShortDesc()));
        			break;
        		}
        	}
        }
    }

    /**
     * Will not check if store is not create yet.
     * Will not check if store association is NONE.
     * Will not check if current store association is same as in the database
     * Will fail the validation if store association changed and entity(user or busEntity) associated
     * with group exists in other store than current store.
     * @param result
     */
    private void checkGroupStoreAssociation(ValidationRuleResult result) {
    	GroupData group = groupView.getGroup();
    	if (group.getGroupId() == null || groupView.getAssocStoreId() == 0)
    		return;
    	
    	GroupService groupService = getGroupService();
    	List<GroupAssocData> gaList = groupService.getGroupAssociation(group.getGroupId(), RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
    	if (gaList.size() > 0 && gaList.get(0).getBusEntityId().equals(groupView.getAssocStoreId())){
    		return;
    	}
    	
    	List<String> storeNames = groupService.getStoreAssociations(group.getGroupId(), group.getGroupTypeCd(), groupView.getAssocStoreId());
    	if (storeNames.size() > 0){
    		String storeNamesStr = "";
    		for (int i = 0; i < storeNames.size(); i++){
    			if (i == 0)
    				storeNamesStr = storeNames.get(i);
    			else if (i == 4){
    				storeNamesStr += "...";
    				break;
    			}else{
    				storeNamesStr += ", " + storeNames.get(i);
    			}    			
    		}
    		result.failed(ExceptionReason.GroupUpdateReason.GROUP_HAS_MULTI_STORE_ASSOCIATION,
                    new StringArgument(group.getShortDesc()),
                    new StringArgument(group.getGroupTypeCd()),
                    new StringArgument(Utility.toCommaString(storeNamesStr)));
    	}
	}
    
    public GroupService getGroupService() {
        return ServiceLocator.getGroupService();
    }

}
