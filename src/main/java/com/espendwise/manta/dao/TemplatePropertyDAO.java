package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.TemplatePropertyData;

import java.util.List;

public interface TemplatePropertyDAO {

    public  List<TemplatePropertyData> updateTemplateProperties(Long templateId, List<TemplatePropertyData> properties);

    public List<TemplatePropertyData> findTemplateProperties(Long templateId);
}
