package com.espendwise.manta.util;


public enum SitePropertyExtraCode implements PropertyExtraCode {

    SHARE_BUYER_GUIDES(RefCodeNames.PROPERTY_TYPE_CD.SHARE_ORDER_GUIDES),
    SITE_REFERENCE_NUMBER(RefCodeNames.PROPERTY_TYPE_CD.SITE_REFERENCE_NUMBER);

    private String nameExtraCode;

    SitePropertyExtraCode(String NameExtraCode) {
        this.nameExtraCode = NameExtraCode;
    }

    public String getNameExtraCode() {
        return nameExtraCode;
    }
}
