package com.espendwise.manta.service;

import java.util.List;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.CMSView;

public interface CMSService {
	
	public CMSView findCMS(Long primaryId);
	
	public EntityHeaderView findPrimaryEntityHeader(Long primaryId);
	
	public BusEntityData findPrimaryEntity(Long primaryId);

	CMSView saveCMS(Long primaryId, CMSView cmsView) throws DatabaseUpdateException;

}
