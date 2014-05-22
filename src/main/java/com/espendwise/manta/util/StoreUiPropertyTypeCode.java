package com.espendwise.manta.util;


public enum StoreUiPropertyTypeCode implements PropertyTypeCode {

    MANTA_UI_LOGO(RefCodeNames.PROPERTY_TYPE_CD.MANTA_UI_LOGO),
    UI_PAGE_TITLE(RefCodeNames.PROPERTY_TYPE_CD.UI_PAGE_TITLE),
    UI_FOOTER(RefCodeNames.PROPERTY_TYPE_CD.UI_FOOTER);

    private String propertyTypeCode;

    StoreUiPropertyTypeCode(String propertyTypeCode) {
          this.propertyTypeCode = propertyTypeCode;
    }

    @Override
    public String getTypeCode() {
        return propertyTypeCode;
    }

    @Override
    public String toString(){
        return propertyTypeCode;
    }

}
