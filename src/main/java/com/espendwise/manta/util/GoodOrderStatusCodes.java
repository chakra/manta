package com.espendwise.manta.util;


public enum GoodOrderStatusCodes implements PropertyStatusCode {

    ORDERED(RefCodeNames.ORDER_STATUS_CD.ORDERED),
    INVOICED(RefCodeNames.ORDER_STATUS_CD.INVOICED),
    ERP_RELEASED(RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED),
    PROCESS_ERP_PO(RefCodeNames.ORDER_STATUS_CD.PROCESS_ERP_PO);

    private String statusCode;

    GoodOrderStatusCodes(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return statusCode;
    }

    }