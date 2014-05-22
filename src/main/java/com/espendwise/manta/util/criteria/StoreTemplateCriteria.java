package com.espendwise.manta.util.criteria;

public class StoreTemplateCriteria extends SimpleCriteria {

    private Long storeId;
    private String  templateId;

    public StoreTemplateCriteria(Long storeId) {
        super();
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
