package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.GenericReportData;

import java.util.List;

public interface GenericReportDAO {

    public List<GenericReportData> findGenericReports(Long groupId);

}
