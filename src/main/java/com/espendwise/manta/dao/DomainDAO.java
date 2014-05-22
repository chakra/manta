package com.espendwise.manta.dao;


import com.espendwise.manta.model.view.DomainView;

import java.util.List;

public interface DomainDAO {

    public Long getDomainId(String domainName);

    public Long getDefaultDomainId();

    public DomainView getDomainView(Long domainId);

    public Long findDomainStore(String domainName);

}
