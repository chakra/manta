package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.ProcessData;

public interface ProcessDAO extends DAO {

	public ProcessData getProcessByName(String processName);


    
}

