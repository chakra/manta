package com.espendwise.manta.util;


public enum ManufacturerPropertyTypeCode implements PropertyTypeCode {

    MSDS_PLUGIN(RefCodeNames.PROPERTY_TYPE_CD.MSDS_PLUGIN);

    private String propertyTypeCode;

    ManufacturerPropertyTypeCode(String propertyTypeCode) {
        this.propertyTypeCode = propertyTypeCode;
    }

    @Override
    public String getTypeCode() {
        return propertyTypeCode;
    }

    @Override
    public String toString() {
        return propertyTypeCode;
    }

}
