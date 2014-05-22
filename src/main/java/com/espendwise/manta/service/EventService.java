package com.espendwise.manta.service;


import java.util.List;
import java.util.Locale;

import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.model.data.EventPropertyData;


public interface EventService {
    public BatchOrderView saveBatchOrder(BatchOrderView batchOrderView, Locale locale) throws Exception;

	public void cancellEvent(List<Long> selected) throws Exception;
	public List<EventPropertyData> getEventProperties(List<Long> selected, List<String> propNames) throws Exception;
	
	public List<BatchOrderView> getBatchOrders(Long storeId, String statusCd) throws Exception;
    public CatalogManagerView saveCatalog(CatalogManagerView catalogView, Locale locale, String eventStatus) throws Exception;
	public List<CatalogManagerView> getCatalogs(Long storeId, String statusCd) throws Exception;
}
