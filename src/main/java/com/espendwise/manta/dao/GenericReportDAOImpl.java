package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.GenericReportData;
import com.espendwise.manta.util.RefCodeNames;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GenericReportDAOImpl extends DAOImpl implements  GenericReportDAO {

    public GenericReportDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GenericReportData> findGenericReports(Long groupId) {

        Query q = em.createQuery("Select report from GenericReportData report, GroupAssocData assoc " +
               "where report.genericReportId = assoc.genericReportId " +
               " and assoc.groupAssocCd = (:groupAssocCd) " +
               " and assoc.groupId = (:groupId)"
       );

        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);
        q.setParameter("groupId", groupId);

        return q.getResultList();
    }

}
