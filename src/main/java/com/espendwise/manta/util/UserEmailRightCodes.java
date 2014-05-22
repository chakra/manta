package com.espendwise.manta.util;



public  enum UserEmailRightCodes implements NamedPropertyTypeCode {

    USER_GETS_EMAIL_ORDER_NEEDS_APPROVAL("ONAe"),
    USER_GETS_EMAIL_ORDER_WAS_REJECTED("ORe"),
    USER_GETS_EMAIL_ORDER_WAS_MODIFIED("OMe"),
    USER_GETS_EMAIL_ORDER_WAS_APPROVED("OAe"),
    USER_GETS_EMAIL_ORDER_DETAIL_APPROVED("ODAe"),
    USER_GETS_EMAIL_ORDER_SHIPPED("OSe"),
    USER_GETS_EMAIL_CUTOFF_TIME_REMINDER("CTRe"),
    USER_GETS_EMAIL_PHYSICAL_INV_NON_COMPL_SITE_LISTING("PINCSLe"),
    USER_GETS_EMAIL_PHYSICAL_INV_COUNTS_PAST_DUE("PICPDe");

    private String typeCode;


    UserEmailRightCodes(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}