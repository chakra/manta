package com.espendwise.manta.web.util;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;

import java.util.List;

public class AccountSiteHierarchyFormBinder {

    public static void addHierarhyEmptyLevels(List<BusEntityData> siteHierLevels, int n) {
        for (int i = 0; i < n; i++) {
            siteHierLevels.add(createHierarhyEmptyLevel(siteHierLevels.size() + 1));
        }
    }


    public  static  BusEntityData createHierarhyEmptyLevel(int number){

        BusEntityData be = new BusEntityData();

        be.setLongDesc(String.valueOf(number));
        be.setShortDesc(Constants.EMPTY);
        be.setBusEntityStatusCd(RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
        be.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL);
        be.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);

        return be;
    }
}
