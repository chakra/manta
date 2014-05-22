package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.UiOptionView;

public class LoginForm extends WebForm implements IModelAttribute, ValidForm {

    private Long storeId;
    private UiOptionView uiOptions;
    private String country;
    private String language;
    private String datasource;

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setUiOptions(UiOptionView uiOptions) {
        this.uiOptions = uiOptions;
    }

    public Long getStoreId() {
        return storeId;
    }

    public UiOptionView getUiOptions() {
        return uiOptions;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDatasource() {
        return datasource;
    }
}
