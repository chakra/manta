package com.espendwise.manta.util;


public class SystemError {

    public static RuntimeException multipleUsers(String a) {
        return new RuntimeException("Multiple users found for username: " + a);
    }

    public static UnsupportedOperationException methodDoesNotSupportObjectOfType(String a) {
        return new UnsupportedOperationException("Method does not support objects of type: " + a);
    }

    public static RuntimeException accountNotFound(Long storeId, Long accountId) {
        return new RuntimeException("Account " + accountId + " for store " + storeId + " not found");
    }

    public static RuntimeException multiplyAssociationsWithLevelObject(Long levelId) {
        return new RuntimeException("Multiple associations with level object found for levelId =" + levelId);
    }

    public static  RuntimeException moreThanOneSiteHierarchyParentLevelFound(Long levelId) {
        return new RuntimeException("More than 1 parent level found for levelId =" + levelId);
    }

    public static RuntimeException siteSiteHierarchyMultipleCnfiguration(Long parentId, Long siteId) {
        return new RuntimeException("Incorrect site configuration. Site is configured for multiple levels of the Site Hierarchy,  Hierarchy ID = "+parentId+", Location ID = "+siteId);
    }
}
