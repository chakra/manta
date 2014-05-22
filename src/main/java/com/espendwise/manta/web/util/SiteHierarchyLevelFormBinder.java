package com.espendwise.manta.web.util;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.*;
import com.espendwise.manta.web.forms.SiteHierarchyLayerLevelForm;
import com.espendwise.manta.web.forms.SiteHierarchyLevelForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SiteHierarchyLevelFormBinder implements Serializable {

    public static List<SiteHierarchyLevelForm> obj2Form(int level, List<BusEntityData> siteHierLevels, List<SiteHierarchyLevelForm> siteHierarchyLevelForms) {

        for (int i = 0, siteHierLevelsSize = siteHierLevels.size(); i < siteHierLevelsSize; i++) {
            BusEntityData siteHierLevel = siteHierLevels.get(i);
            siteHierarchyLevelForms.add(obj2Form(siteHierLevel, i + 1, level, new SiteHierarchyLevelForm()));
        }

        return siteHierarchyLevelForms;
    }


    public static SiteHierarchyLevelForm createHierarhyEmptyLevel(int number) {

        SiteHierarchyLevelForm x = new SiteHierarchyLevelForm();

        x.setNumber(String.valueOf(number));
        x.setLongDesc(String.valueOf(number));

        return x;
    }

    private static SiteHierarchyLevelForm obj2Form(BusEntityData busEntityData, int number, int level, SiteHierarchyLevelForm siteHierarchyLevelForm) {

        SiteHierarchyLevelForm x = new SiteHierarchyLevelForm();

        x.setBusEntityId(busEntityData.getBusEntityId());
        x.setNumber(String.valueOf(number));
        x.setLongDesc(busEntityData.getLongDesc());
        x.setLevel(String.valueOf(level));
        x.setName(busEntityData.getShortDesc());

        return x;
    }

    public static boolean isLevelCanBeManaged(List<SiteHierarchyLevelForm> levelCollection) {
        for (SiteHierarchyLevelForm x : levelCollection) {
            if (Utility.longNN(x.getBusEntityId()) > 0) {
                return true;
            }
        }
        return false;
    }


    public static UpdateRequest<BusEntityData> createUpdateObject(boolean topLevel,
                                                                  List<SiteHierarchyLevelForm> levelCollection,
                                                                  List<BusEntityData> dbSiteHierLevels) {

        UpdateRequest<BusEntityData> updRequest = new UpdateRequest<BusEntityData>(
                Utility.emptyList(BusEntityData.class),
                Utility.emptyList(BusEntityData.class),
                Utility.emptyList(BusEntityData.class)
        );


        Map<Long, BusEntityData> entitiesMap = Utility.toMap(dbSiteHierLevels);

        if (Utility.isSet(levelCollection)) {

            for (SiteHierarchyLevelForm x : levelCollection) {
                BusEntityData dbObj = entitiesMap.get(x.getBusEntityId());
                if (dbObj == null && Utility.isSet(x.getName())) {
                    updRequest.getToCreate().add(form2Object(topLevel, x, (BusEntityData) null));
                } else if (dbObj != null && !Utility.isSet(x.getName())) {
                    updRequest.getToDelete().add(dbObj);
                } else if (dbObj != null && Utility.isSet(x.getName())) {
                    updRequest.getToUpdate().add(form2Object(topLevel, x, dbObj));
                }
            }

        }

        return updRequest;
    }

    private static BusEntityData form2Object(boolean topLevel, SiteHierarchyLevelForm x, BusEntityData busEntityData) {

        if (busEntityData == null) {
            busEntityData = new BusEntityData();
            busEntityData.setBusEntityStatusCd(RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
            busEntityData.setBusEntityTypeCd(topLevel ? RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL : RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT);
            busEntityData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
            busEntityData.setLongDesc(x.getLongDesc());
            busEntityData.setLocaleCd(Constants.UNK);
        }

        busEntityData.setShortDesc(x.getName());
        busEntityData.setLongDesc(x.getLongDesc());

        return busEntityData;
    }

    public static Long findEndSiteHierarchyLevelId(List<SiteHierarchyLevelForm> levelCollection) {
        Long id = null;
        for (SiteHierarchyLevelForm level : levelCollection) {
            if (Utility.longNN(level.getBusEntityId()) > 0) {
                id = level.getBusEntityId();
            }
        }
        return id;
    }

    public static List<?> levelIds(List<SiteHierarchyLayerLevelForm> layerLevels) {
        List<Long> x = new ArrayList<Long>();
        if (Utility.isSet(layerLevels)) {
            for (SiteHierarchyLayerLevelForm level : layerLevels) {
                x.add(level.getLevelId());
            }
        }
        return x;
    }
}
