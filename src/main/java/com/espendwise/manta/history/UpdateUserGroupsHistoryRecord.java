package com.espendwise.manta.history;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.UserGroupsView;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

public class UpdateUserGroupsHistoryRecord extends HistoryRecord {

	@Override
	public String getHistoryTypeCode() {
		return HistoryRecord.TYPE_CODE_UPDATE_USER_GROUPS;
	}

	@Override
	public String getShortDescription() {
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.userGroups");
		Object[] args = new Object[1];
		args[0] = objectType;
		return I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.modifyObject", args);
	}
	
	@Override
	public String getLongDescription() {
		return buildLongDescription(false);
	}
	
	@Override
	public String getLongDescriptionAsHtml() {
		return buildLongDescription(true);
	}
	
	private String buildLongDescription(boolean includeHtmlMarkup) {
		StringBuilder returnValue = new StringBuilder(200);
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.userGroups");
		Object[] args = new Object[1];
		args[0] = objectType;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.modifiedObject", args));
		if (Utility.isSet(getAttribute01()) && Utility.isSet(getAttribute02())) {
			returnValue.append(" ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.for"));
			returnValue.append(" ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.user").toLowerCase());
			returnValue.append(" '");
			returnValue.append(getAttribute02());
			returnValue.append("'");
			returnValue.append(" (");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
			returnValue.append(" ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute01(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER));
			}
			else {
				returnValue.append(getAttribute01());				
			}
			returnValue.append(")");
		}
		returnValue.append(".");
		returnValue.append(getGroupOutput(includeHtmlMarkup, "history.description.addedGroups", getClob1()));
		returnValue.append(getGroupOutput(includeHtmlMarkup, "history.description.removedGroups", getClob2()));

		if (includeHtmlMarkup) {
			return htmlEscape(returnValue.toString());
		}
		else {
			return returnValue.toString();
		}
	}

	private String getGroupOutput(boolean includeHtmlMarkup, String description, String groups) {
		StringBuilder returnValue = new StringBuilder();
		Map<String, String> groupMap = getGroupMap(groups);
		if (Utility.isSet(groupMap)) {
			if (includeHtmlMarkup) {
				returnValue.append("<br>");
			}
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), description));
			returnValue.append(": ");
			Iterator<String> groupIterator = groupMap.keySet().iterator();
			boolean includeSeparator = false;
			String separator = ", ";
			while (groupIterator.hasNext()) {
				if (includeSeparator) {
					returnValue.append(separator);
				}
				includeSeparator = true;
				String groupName = groupIterator.next();
				String groupId = groupMap.get(groupName);
				returnValue.append(groupName);
				returnValue.append(" (");
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
				returnValue.append(" ");
				if (includeHtmlMarkup) {
					returnValue.append(getHistoryLink(groupId, RefCodeNames.HISTORY_OBJECT_TYPE_CD.GROUP));
				}
				else {
					returnValue.append(groupId);
				}
				returnValue.append(")");
			}
		}
		return returnValue.toString();
	}
	
	private Map<String, String> getGroupMap(String groups) {
		Map<String, String> returnValue = new HashMap<String, String>();
		if (Utility.isSet(groups)) {
			String[] tokens = groups.split(clobValueSeparator);
			for (int i=0; i < tokens.length; i=i+2) {
				returnValue.put(tokens[i+1], tokens[i]);
			}
		}
		return new TreeMap<String, String>(returnValue);
	}

	@Override
	public Set<HistoryObjectData> getInvolvedObjects(Object... objects) {
		Set<HistoryObjectData> returnValue = new HashSet<HistoryObjectData>();
		//this method should receive a single object - a UserGroupsView - containing
		//the information to be logged in the history record
		UserGroupsView userGroupsView = (UserGroupsView) objects[0];
		
		UserData user = userGroupsView.getUser();
		//created an involved object for the user
		if (Utility.isSet(user)) {
			HistoryObjectData userGroupsObject = new HistoryObjectData();
			userGroupsObject.setObjectId(user.getUserId());
			userGroupsObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER);
			returnValue.add(userGroupsObject);
		}
		
		//create an involved object for every assigned group
		List<GroupData> assignedGroups = userGroupsView.getAssignedGroups();
		if (Utility.isSet(assignedGroups)) {
			for (GroupData assignedGroup : assignedGroups) {
				HistoryObjectData userGroupsObject = new HistoryObjectData();
	    		userGroupsObject.setObjectId(assignedGroup.getGroupId());
	    		userGroupsObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.GROUP);
	    		returnValue.add(userGroupsObject);
			}
		}
		
		//create an involved object for every unassigned group
		List<GroupData> unassignedGroups = userGroupsView.getUnassignedGroups();
		if (Utility.isSet(unassignedGroups)) {
			for (GroupData unassignedGroup : unassignedGroups) {
				HistoryObjectData userGroupsObject = new HistoryObjectData();
	    		userGroupsObject.setObjectId(unassignedGroup.getGroupId());
	    		userGroupsObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.GROUP);
	    		returnValue.add(userGroupsObject);
			}
		}
		
		return returnValue;
	}

	@Override
	public Set<HistorySecurityData> getSecurityObjects(Object... objects) {
		Set<HistorySecurityData> returnValue = new HashSet<HistorySecurityData>();
		//this method should receive a single object - a UserGroupsView - containing
		//the information to be logged in the history record
		UserGroupsView userGroupsView = (UserGroupsView) objects[0];
		List<BusEntityData> userStores = userGroupsView.getUserStores();
		//create a security data object for every store to which the user is associated
		if (Utility.isSet(userStores)) {
			for (BusEntityData userStore : userStores) {
	    		HistorySecurityData userGroupsSecurity = new HistorySecurityData();
	    		userGroupsSecurity.setBusEntityId(userStore.getBusEntityId());
	    		userGroupsSecurity.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
	    		returnValue.add(userGroupsSecurity);
			}
		}
		return returnValue;
	}

	@Override
	public void describeIntoHistoryData(HistoryData historyData, Object... objects) {
		super.describeIntoHistoryData(historyData, objects);
		//this method should receive a UserGroupsView containing the ids of the objects
		//to be logged in the history record
		UserGroupsView userGroupsView = (UserGroupsView) objects[0];
		
		//store the user id in attribute1 and the user name in attribute2
		UserData user = userGroupsView.getUser();
		if (Utility.isSet(user)) {
	    	historyData.setAttribute01(user.getUserId().toString());
	    	historyData.setAttribute02(user.getUserName());
		}
		
		//store the assign group info in clob1
		List<GroupData> assignedGroups = userGroupsView.getAssignedGroups();
		if (Utility.isSet(assignedGroups)) {
			historyData.setClob1(getGroupInfo(assignedGroups));
		}
		
		//store the assign group info in clob2
		List<GroupData> unassignedGroups = userGroupsView.getUnassignedGroups();
		if (Utility.isSet(unassignedGroups)) {
			historyData.setClob2(getGroupInfo(unassignedGroups));
		}
	}
	
	private String getGroupInfo(List<GroupData> groups) {
		StringBuilder returnValue = new StringBuilder();
		if (Utility.isSet(groups)) {
			Map<String, Long> groupMap = new HashMap<String, Long>();
			Iterator<GroupData> groupIterator = groups.iterator();
			while (groupIterator.hasNext()) {
				GroupData group = groupIterator.next();
				groupMap.put(group.getShortDesc(), group.getGroupId());
			}
			Map<String, Long> sortedGroupMap = new TreeMap<String, Long>(groupMap);
			Iterator<String> sortedGroupMapIterator = sortedGroupMap.keySet().iterator();
			boolean includeSeparator = false; 
			while (sortedGroupMapIterator.hasNext()) {
				String groupName = sortedGroupMapIterator.next();
				Long groupId = sortedGroupMap.get(groupName);
				if (includeSeparator) {
					returnValue.append(clobValueSeparator);
				}
				includeSeparator = true;
				returnValue.append(groupId);
				returnValue.append(clobValueSeparator);
				returnValue.append(groupName);
			}
		}
		return returnValue.toString();
	}

}
