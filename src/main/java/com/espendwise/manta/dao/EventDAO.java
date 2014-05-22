package com.espendwise.manta.dao;

import java.io.IOException;
import java.util.List;

import com.espendwise.manta.model.data.EventEmailData;
import com.espendwise.manta.model.data.EventPropertyData;
import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.EventEmailView;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.util.FileAttach;


public interface EventDAO extends DAO {
	public BatchOrderView saveBatchOrder(BatchOrderView batchOrderView) throws Exception;
	public void cancellEvent(List<Long> selected) throws Exception;	
	public List<BatchOrderView> getBatchOrders(Long storeId, String statusCd) throws Exception;
	public EventEmailView addEventEmail(EventEmailData eventEmailData, FileAttach[] fileAttachments) throws IOException;
	public CatalogManagerView saveCatalog(CatalogManagerView catalogView, String eventStatus) throws Exception;
	public List<CatalogManagerView> getCatalogs(Long storeId, String statusCd) throws Exception;
	public List<EventPropertyData> getEventProperties(List<Long> selected, List<String> propNames) throws Exception;	
}

