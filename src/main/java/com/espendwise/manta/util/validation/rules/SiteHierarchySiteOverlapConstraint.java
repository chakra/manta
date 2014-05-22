package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;

import java.util.*;


public class SiteHierarchySiteOverlapConstraint implements ValidationRule {


    private List<Long> sitesIds;
    private Long hierarchyId;

    public SiteHierarchySiteOverlapConstraint(List<Long> sitesIds, Long hierarchyId) {
        this.sitesIds = sitesIds;
        this.hierarchyId = hierarchyId;
    }

    public SiteHierarchySiteOverlapConstraint(Long hierarchyId, UpdateRequest<Long> longUpdateRequest) {
        this.sitesIds = longUpdateRequest.getToCreate();
        this.hierarchyId = hierarchyId;
    }

    public List<Long> getSitesIds() {
        return sitesIds;
    }

    @Override
    public ValidationRuleResult apply() {

        ValidationRuleResult vr = new ValidationRuleResult();

        java.util.List<BusEntityAssocData> configured = ServiceLocator.getSiteHierarchyService().findConfiguredSitesNotFor(
                getHierarchyId(),
                getSitesIds()
        );


        Set<String> errors = new HashSet<String>();
        Map<String, Set<String>> errMap = new HashMap<String, Set<String>>();

        if (configured != null) {

            for (BusEntityAssocData c : configured) {

                String cId = c.getBusEntity1Id().toString();
                String pId = c.getBusEntity2Id().toString();

                if (!errMap.containsKey(pId)) {
                    errors = new HashSet<String>();
                    errMap.put(pId, errors);
                } else {
                    errors = errMap.get(pId);
                }

                errors.add(cId);

            }

        }

        if (!errors.isEmpty()) {

            vr.failed(
                    ExceptionReason.SiteHierarchyUpdateReason.SITE_HIER_ERROR_OVERLAPING_SITE_CONFIG,
                    new ObjectArgument<Map>(errMap)
            );

            return vr;

        }

        vr.success();

        return vr;

    }

    public Long getHierarchyId() {
        return hierarchyId;
    }
}
