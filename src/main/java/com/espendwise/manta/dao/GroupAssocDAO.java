package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.GroupAssocData;

import java.util.Collection;
import java.util.List;

public interface GroupAssocDAO {

    public List<Long> findUserGroupIds(Long userIds);

    public List<GroupAssocData> findFunctionsByGroupIds(Collection<Long> groupIds);

    public void removeUserGroupAssociation(Long userId, List<Long> groupIds);

    public void addUserGroupAssociation(Long userId, Long groupId);
    public void removeStoreGroupAssociation(Long groupId);
    public void addStoreGroupAssociation(Long userId, Long groupId);
    public void addReportGroupAssociation(List<Long> reportIds, Long groupId);
    public void removeReportGroupAssociation(List<Long> reportIds, Long groupId);
    public void addFunctionGroupAssociation(List<String> functionNames, Long groupId);
    public void removeFunctionGroupAssociation(List<String> functionNames, Long groupId);
    public void addGroupAssociation(List assocKeys, Long groupId, String groupAssocCd);
    public void removeGroupAssociation(List assocKeys, Long groupId, String groupAssocCd);
    public List<GroupAssocData> getGroupAssociation(Long groupId, String groupAssocCd);
    public List<GroupAssocData> updateUserGroupAssocs(Long userId, List<GroupAssocData> newAssocs);
}
